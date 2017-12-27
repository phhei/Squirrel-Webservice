package comSquirrel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import comSquirrel.rabbit.RabbitMQList;

@SpringBootApplication
public class Application {

    public static RabbitMQList listenerThread;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        listenerThread = new RabbitMQList();
        listenerThread.run();
    }
}
