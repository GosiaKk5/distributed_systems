public class Starter {
    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[1]);

        switch(args[0]){
            case "s":
                Server server = new Server(portNumber);
                server.start();
                break;

            case "c":
                String nickname = args[2];
                Client client = new Client(portNumber, nickname);
                client.start();
            default:

        }
    }
}