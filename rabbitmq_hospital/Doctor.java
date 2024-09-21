import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Doctor {
    List<String> availableTests = Arrays.asList("hip", "knee", "elbow");
    private final String name;
    private Channel channel;
    private String queueName;

    public Doctor(String name){
        this.name = name;
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initQueues() {

        try {
            for(String test: availableTests) {
                channel.queueDeclare(test + "-queue", false, false, false, null);
            }
            queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, "logs-exchange", "");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void startListening(){
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body){
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("Received " + message);
            }
        };

        try {
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String patientName, String testType){
        if(availableTests.contains(testType)){
            try {
                String message = testType + " " + patientName + " " + queueName;
                channel.basicPublish("", testType + "-queue", null, message.getBytes(StandardCharsets.UTF_8));

                String logMessage = "Doctor " + name + ": " + message;
                channel.basicPublish("", "admin-queue", null, logMessage.getBytes(StandardCharsets.UTF_8));

                System.out.println("Sent: " + message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else System.out.println("Wrong test type. You can choose: hip, knee, elbow");
    }

    public static void main(String[] argv){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter doctor name:");
        String doctorName = scanner.nextLine();
        Doctor doctor = new Doctor(doctorName);

        System.out.println("-------------------------------");
        System.out.println("DOCTOR " + doctorName);
        System.out.println("-------------------------------");

        String input = null;
        String[] input_list = null;

        while (true){
            System.out.println("Enter new order [name test_type]");
            input = scanner.nextLine();
            input_list = input.split(" ");

            if(input_list.length != 2){
                System.out.println("Wrong number of arguments!");
            }
            else{
                doctor.sendMessage(input_list[0], input_list[1]);
            }
        }

    }
}


