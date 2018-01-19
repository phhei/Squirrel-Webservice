package comSquirrel.rabbit;

import com.SquirrelWebObject;
import com.SquirrelWebObjectHelper;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * The interface between the RabbitMQ and the Web-Service
 * @author Philipp Heinisch
 */
public class RabbitMQList implements Runnable {

    private List<SquirrelWebObject> dataQueue = new ArrayList<>();
    private final static String QUEUE_NAME = "squirrel.web";
    private Connection connection;
    private Channel channel;

    private Logger logger = LoggerFactory.getLogger(RabbitMQList.class);

    private boolean rabbitConnect(int triesLeft) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbit");
        factory.setUsername("guest");
        factory.setPassword("guest");
        connection = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            if (triesLeft > 0) {
                logger.warn(triesLeft + " tries left: Could not established a connection to the rabbit: no communication to rabbit :( [" + e.getMessage() + "]", e);
                try {
                    //wait until the rabbit is started in Docker
                    Thread.sleep(5000);
                } catch (InterruptedException ei) {
                    logger.info("The waiting time for the rabbit was interrupted. Steo forward with trying to get a connection!");
                }
                return rabbitConnect(triesLeft-1);
            } else {
                logger.error("0 tries left: Could not established a connection to the rabbit: no communication to rabbit :( [" + e.getMessage() + "]", e);
                return false;
            }
        } catch (TimeoutException e) {
            if (triesLeft > 0) {
                logger.warn(triesLeft + " tries left: Could not established a connection to the rabbit - TIMEOUT: no communication to rabbit :( [" + e.getMessage() + "]", e);
                try {
                    //wait until the rabbit is started in Docker
                    Thread.sleep(10000);
                } catch (InterruptedException ei) {
                    logger.info("The waiting time for the rabbit was interrupted. Steo forward with trying to get a connection!");
                }
                return rabbitConnect(triesLeft-1);
            } else {
                logger.error("0 tries left: Could not established a connection to the rabbit - TIMEOUT: no communication to rabbit :( [" + e.getMessage() + "]", e);
                return false;
            }
        }

        logger.info("Connection to rabbit succeeded: " + factory.getHost() + " - " + connection.getClientProvidedName() + " [" + connection.getId() + "]");
        return true;
    }

    @Override
    public void run() {
        if (rabbitConnect(6)) {
            try {
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            } catch (IOException e) {
                logger.error("I have a connection to " + connection.getClientProvidedName() + " with the channel number " + channel.getChannelNumber() + ", but I was not able to declare a queue :(", e);
                return;
            }
            logger.info("Queue declaration succeeded with the name " + QUEUE_NAME + " [" + channel.getChannelNumber() + "]");
        } else {
            return;
        }

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                SquirrelWebObject o = SquirrelWebObjectHelper.convertToObject(body);
                logger.debug("The consumer " + consumerTag + "received an SquirrelWebObject from the Frontier!");
                dataQueue.add(o);
                logger.trace("Added the new SquirrelWebObject to the dataQueue, contains " + dataQueue.size() + " SquirrelWebObjects now!");
            }
        };
        try {
//            try {
//                Thread.sleep(15000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            try {
               Thread.sleep(5000);
            } catch (InterruptedException ei) {
                ei.printStackTrace();
                return;
            }
            run();
        }
    }

    public void close() throws IOException, TimeoutException {
        if (channel == null)
            return;
        logger.debug("THREAD: CLOSE BEGIN");
        channel.close();
        logger.debug("THREAD: CLOSE END");
    }

    /**
     * Gets the fetched data from the Frontier. Contains many information about the current crawling status and so on
     * @return the latest {@link SquirrelWebObject}
     */
    public SquirrelWebObject getSquirrel() {
        return getSquirrel(dataQueue.size()-1);
    }

    /**
     * Gets the fetched data from the Frontier. Contains many information about the current crawling status and so on
     * @param index All received {@link SquirrelWebObject} are stored in a list. Index {@code 0} is the oldest entry, Index {@code size-1} is the latest one
     * @return the {@link SquirrelWebObject}
     */
    public SquirrelWebObject getSquirrel(int index) {
        if (dataQueue.isEmpty()) {
            return new SquirrelWebObject();
        }
        try {
            return dataQueue.get(index);
        } catch (IndexOutOfBoundsException e) {
            return dataQueue.get(dataQueue.size()-1);
        }
    }
}