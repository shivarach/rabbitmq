package org.shiva.worker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NewTaskPublisher2 {
    private static final String EXCHANGE_NAME = "jobs";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String routingKey = "green";

            //update message run multiple times to produce more messages to rabbitmq
            String message = "message.with routing key green.where.consumption is faked by.thread.sleep.with each dot";

            channel.basicPublish(EXCHANGE_NAME, routingKey,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));
            System.out.printf(" [x] Sent '%s' with routing key %s%n", message, routingKey);
        }
    }
}
