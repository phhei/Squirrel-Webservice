package comSquirrel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import comSquirrel.rabbit.RabbitMQList;

/**
 * The starting pount for the Web-Service
 * @author Philipp Heinisch
 */
@SpringBootApplication
public class Application {

    public static RabbitMQList listenerThread;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        listenerThread = new RabbitMQList();
        listenerThread.run();
    }
}
