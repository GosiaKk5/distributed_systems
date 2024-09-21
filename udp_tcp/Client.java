import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Client {
    private final int MULTICAST_PORT = 6_789;
    private final String MULTICAST_IP = "228.5.6.7";

    private int serverPort;
    private String nickname;

    public Client(int serverPort, String nickname){
        this.serverPort = serverPort;
        this.nickname = nickname;
    }

    public void start(){
        System.out.println("CHAT CLIENT STARTED");
        System.out.println("Welcome " + nickname + "!");

        String hostName = "localhost";
        Socket tcpSocket = null;
        DatagramSocket udpSocket = null;
        MulticastSocket multicastSocket = null;

        try {
            // tcp udp sockets
            tcpSocket = new Socket(hostName, serverPort);
            udpSocket = new DatagramSocket(tcpSocket.getLocalPort());

            //multicast socket
            multicastSocket = new MulticastSocket(MULTICAST_PORT);
            SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(MULTICAST_IP), MULTICAST_PORT);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());

            multicastSocket.joinGroup(socketAddress, networkInterface);

            initCommunication(tcpSocket, udpSocket, multicastSocket);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tcpSocket != null){
                try {
                    tcpSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if(udpSocket != null){
                udpSocket.close();
            }

        }
    }

    private void initCommunication(Socket tcpSocket, DatagramSocket udpSocket,MulticastSocket multicastSocket) throws IOException {
        // in & out tcp streams
        PrintWriter out = new PrintWriter(tcpSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));

        //send nick
        out.println(nickname);

        //create threads
        Thread tcpReceiveThread = createTcpReceiveThread(in);
        Thread udpReceiveThread = createUdpReceiveThread(udpSocket);
        Thread multicastReceiveThread = createMulticastReceiveThread(multicastSocket);
        Thread sendThread = createSendThread(out, udpSocket, multicastSocket);

        //start threads
        tcpReceiveThread.start();
        udpReceiveThread.start();
        multicastReceiveThread.start();
        sendThread.start();

        try {
            tcpReceiveThread.join();
            udpReceiveThread.join();
            multicastReceiveThread.join();
            sendThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private Thread createTcpReceiveThread(BufferedReader in){
        return new Thread(() -> {
            try {
                String serverLine;
                while ((serverLine = in.readLine()) != null) {
                    System.out.println(serverLine + "\n");
                }
            } catch (IOException e) {
                System.out.println("Connection to server is lost!");
                System.exit(1);
            }
        });

    }

    private Thread createUdpReceiveThread(DatagramSocket udpSocket){
        return new Thread(() -> {
            byte[] receiveBuffer = new byte[1024];

            while(true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                try {
                    udpSocket.receive(receivePacket);
                    String message = new String(receivePacket.getData());
                    System.out.println("UDP " + message);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private Thread createMulticastReceiveThread(MulticastSocket multicastSocket){
        return new Thread(() -> {
            byte[] receiveBuffer = new byte[1024];

            while(true){
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                try {
                    multicastSocket.receive(receivePacket);
                    String message = new String(receivePacket.getData());
                    System.out.println("MULTICAST " + message);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private Thread createSendThread(PrintWriter out, DatagramSocket udpSocket, MulticastSocket multicastSocket){
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        return new Thread(() -> {
            try {
                String consoleLine;
                while ((consoleLine = consoleReader.readLine()) != null) {
                    String[] input = consoleLine.split(" ", 2);

                    if(input.length == 2){
                        String messageType = input[0];
                        String messageText = nickname + ": " + input[1];

                        switch (messageType){
                            case "T":
                                out.println(messageText);
                                break;

                            case "U":
                                String catAsciiArt = "\n|\\---/|\n| o_o |\n \\_^_/\n";
                                String udpMessage = messageText + catAsciiArt ;
                                InetAddress address = InetAddress.getByName("localhost");
                                byte[] sendBuffer = udpMessage.getBytes();
                                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, serverPort);
                                udpSocket.send(sendPacket);
                                break;

                            case "M":
                                String flowerAsciiArt =
                                        "\n      .--.  \n" +
                                                "    .'_\\/_'.\n" +
                                                "    '. /\\ .'\n" +
                                                "      \"||\"\n" +
                                                "       || /\\\n" +
                                                "    /\\ ||//\\)\n" +
                                                "   (/\\\\||/\n" +
                                                "_____\\\\||/_______\n";
                                String multicastMessage = messageText + flowerAsciiArt;
                                DatagramPacket datagramPacket = new DatagramPacket(multicastMessage.getBytes(),
                                        multicastMessage.length(), InetAddress.getByName(MULTICAST_IP), MULTICAST_PORT);
                                multicastSocket.send(datagramPacket);
                                break;

                            default:
                                System.out.println("Wrong Command! Available commands: T - tcp, U - udp, M - multicast");

                        }
                    }else{
                        System.out.println("Wrong input");
                    }

                }
            } catch (IOException e) {
                System.out.println("Connection to server is lost!");
                System.exit(1);
            }
        });
    }
}
