package org.shiva.worker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Worker2 {
    private static final String EXCHANGE_NAME = "jobs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        //create exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        // bind a queue to channel
        String bindingKey = "green";
        channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
        // on rabbitmq server failure, stores messages on disk
        boolean messageDurable = true;
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // fair dispatch i.e. won't overload one consumer (default is round-robin)
        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } finally {
                System.out.println(" [x] Done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        /*means manual acknowledgement, if ack is not received for message, rabbitmq will enqueue same message
        otherwise marks for delete*/
        boolean autAcknowledgement = false;
        channel.basicConsume(queueName, autAcknowledgement, deliverCallback, consumerTag -> { });
    }

    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
