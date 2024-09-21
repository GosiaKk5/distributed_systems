import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpConnection implements Runnable{
    private Socket clientSocket;
    private Server server;
    private PrintWriter out;
    private BufferedReader in;
    private String nickname;
    public TcpConnection(Socket clientSocket, Server server){
        this.clientSocket = clientSocket;
        this.server = server;
        try {
            // in & out streams
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //get client nickname
            this.nickname = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                String message = in.readLine();
                if(message != null){
                    server.tcpSendAll(message);
                }

            } catch (IOException e) {
                System.out.println(nickname + " left the chat :(");
                break;
            }
        }
    }
    public void send(String message){
        out.println("TCP "  + message);
    }

    public String getNickname() {
        return nickname;
    }

}
