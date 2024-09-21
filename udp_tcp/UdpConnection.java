import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpConnection implements Runnable{
    private DatagramSocket serverUdpSocket;
    private Server server;

    public UdpConnection(DatagramSocket serverUdpSocket, Server server){
        this.serverUdpSocket = serverUdpSocket;
        this.server = server;
    }

    @Override
    public void run() {
        byte[] receiveBuffer = new byte[1024];
        while(true){
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            try {
                serverUdpSocket.receive(receivePacket);
                String msg = new String(receivePacket.getData());
                server.udpSendAll(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

}
