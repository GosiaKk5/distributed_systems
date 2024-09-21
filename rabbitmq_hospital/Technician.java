import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Technician {
    private final String testType1;
    private final String testType2;
    private Channel channel;
    private String queueName;

    public Technician(String testType1, String testType2){
        this.testType1 = testType1;
        this.testType2 = testType2;
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
            channel.queueDeclare(testType1 + "-queue", false, false, false, null);
            channel.queueDeclare(testType2 + "-queue", false, false, false, null);

            queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, "logs-exchange", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void startListening() {
        Random random = new Random();
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);

                if(envelope.getExchange().equals("logs-exchange")){
                    System.out.println(message);
                }else if(envelope.getRoutingKey().equals(testType1 + "-queue") ||
                        envelope.getRoutingKey().equals(testType2 + "-queue")){

                    String[] args = message.split(" ");
                    String testType = args[0];
                    String patientName = args[1];
                    String doctorQueueName = args[2];
                    System.out.println("Stared: " + testType + " test for " + patientName);

                    try {
                        Thread.sleep(random.nextInt(10)*100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Finished: " + testType + " test for " + patientName);

                    String returnMessage = testType + " test results for " + patientName;
                    channel.basicPublish("", doctorQueueName, null, returnMessage.getBytes(StandardCharsets.UTF_8));

                    String logMessage = "Technician: " + returnMessage;
                    channel.basicPublish("", "admin-queue", null, logMessage.getBytes(StandardCharsets.UTF_8));

                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        try {
            channel.basicConsume(testType1 + "-queue", false, consumer);
            channel.basicConsume(testType2 + "-queue", false, consumer);
            channel.basicConsume(queueName, false, consumer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] argv) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter first technician specialization: ");
        String testType1 = br.readLine();
        System.out.println("Enter second technician specialization: ");
        String testType2 = br.readLine();
        System.out.println("-----------------------------------------");
        System.out.println("TECHNICIAN " + testType1 + " " + testType2);
        System.out.println("-----------------------------------------");

        new Technician(testType1, testType2);
}
}
