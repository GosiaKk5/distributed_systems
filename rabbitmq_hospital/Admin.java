import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Admin {
    private Channel channel;

    public Admin(){
        initChannel();
        initQueues();
        startListening();
    }

    private void initChannel(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare("logs-exchange", BuiltinExchangeType.DIRECT);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initQueues() {
        try {
            channel.queueDeclare("admin-queue", false, false, false, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startListening(){
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println( "LOG " + message);
            }
        };

        try {
            channel.basicConsume("admin-queue", true, consumer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCommunicat(String communicat){
        String message = "ADMIN: " + communicat;
        try {
            channel.basicPublish("logs-exchange", "", null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] argv){
        Scanner scanner = new Scanner(System.in);
        Admin admin = new Admin();

        System.out.println("-------------------------------");
        System.out.println("ADMIN");
        System.out.println("-------------------------------");

        String communicat = null;
        while (true){
            System.out.println("Enter communicat:");
            communicat = scanner.nextLine();


            admin.sendCommunicat(communicat);
        }

    }
}


