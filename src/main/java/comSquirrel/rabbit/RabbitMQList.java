package comSquirrel.rabbit;

import com.SquirrelWebObject;
import com.SquirrelWebObjectHelper;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class RabbitMQList extends Thread {

    private List<SquirrelWebObject> dataQueue = new ArrayList<>();
    private final static String QUEUE_NAME = "squirrel.web";
    private Channel channel;

    private Logger logger = LoggerFactory.getLogger(RabbitMQList.class);


    @Override
    public void start() {
        try {
            //wait until the rabbit is started in Docker
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            logger.info("The waiting time for the rabbit was interrupted. Steo forward with trying to get a connection!");
        }
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbit");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            logger.error("Could not established a connection to the rabbit: no communication to rabbit :( [" + e.getMessage() + "]", e);
            return;
        } catch (TimeoutException e) {
            logger.error("Could not established a connection to the rabbit - TIMEOUT: no communication to rabbit :( [" + e.getMessage() + "]", e);
            return;
        }

        logger.info("Connection to rabbit succeeded: " + factory.getHost() + " - " + connection.getClientProvidedName() + " [" + connection.getId() + "]");

        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (IOException e) {
            logger.error("I have a connection to " + connection.getClientProvidedName() + " with the channel number " + channel.getChannelNumber() + ", but I was not able to declare a queue :(", e);
            return;
        }
        logger.info("Queue declaration succeeded with the name " + QUEUE_NAME + " [" + channel.getChannelNumber() + "]");
    }

    @Override
    public void run() {
        if (channel == null)
            return;

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
            //TODO: implement retry's
        }
    }

    public void close() throws IOException, TimeoutException {
        if (channel == null)
            return;
        logger.debug("THREAD: CLOSE BEGIN");
        channel.close();
        logger.debug("THREAD: CLOSE END");
    }

    public SquirrelWebObject getData() {
        if (dataQueue.isEmpty()) {
            return null;
        }
        return dataQueue.get(dataQueue.size()-1);
    }

 /*   private SquirrelWebObject convertBytesToObject(byte[] data) {
        try {
            Object obj = null;
            ByteArrayInputStream bis = null;
            ObjectInputStream ois = null;
            try {
                bis = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bis);
                obj = ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }                }
            }
            return (SquirrelWebObject)obj;
        } catch (ClassCastException e1) {
            logger.warn("There was a converting error! Bytestream of length " + data.length + " has a wrong format/ is incomplete : " + e1.getMessage()+ ". Will return instead an empty WebSquirrelObject!", e1);
            return new SquirrelWebObject();
        }
    }*/
}