import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int portNumber;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private ArrayList<TcpConnection> connections;
    private ArrayList<Socket> clientsSockets;
    private Object connectionsLock;
    private Object socketsLock;
    private DatagramSocket serverUdpSocket;


    public Server(int portNumber){
        this.portNumber = portNumber;
        this.connections = new ArrayList<>();
        this.clientsSockets = new ArrayList<>();
        this.connectionsLock = new Object();
        this.socketsLock = new Object();
        this.serverUdpSocket = null;
    }

    public void start (){
        System.out.println("CHAT SERVER STARTED ON PORT: " + portNumber);
        ServerSocket serverTcpSocket = null;

        try{
            //create sockets
            serverTcpSocket = new ServerSocket(portNumber);
            serverUdpSocket = new DatagramSocket(portNumber);

            //start udp thread
            UdpConnection udpConnection = new UdpConnection(serverUdpSocket, this);
            Thread udpConnectionThread = new Thread(udpConnection);
            udpConnectionThread.start();

            while(true){
                //accept new client
                Socket newClientSocket = serverTcpSocket.accept();
                TcpConnection newConnection = new TcpConnection(newClientSocket, this);
                System.out.println("New client connected - hello " + newConnection.getNickname() + "!");
                synchronized (connectionsLock){
                    connections.add(newConnection);
                }
                synchronized (socketsLock){
                    clientsSockets.add(newClientSocket);
                }
                executorService.execute(newConnection);
            }

        } catch (IOException e) {
            executorService.shutdown();
            throw new RuntimeException(e);
        }
    }

    public void tcpSendAll(String message){
        synchronized (connectionsLock){
            for(TcpConnection connection: connections){
                connection.send(message);
            }
        }
    }

    public void udpSendAll(String message){
        byte[] sendBuffer = message.getBytes();

        synchronized (socketsLock){
            for(Socket clientSocket: clientsSockets){
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientSocket.getInetAddress(), clientSocket.getPort());
                try {
                    serverUdpSocket.send(sendPacket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
