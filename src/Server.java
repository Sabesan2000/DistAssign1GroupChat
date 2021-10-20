import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final ServerSocket serverSocket;
    //Listen for incoming connections from the client

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void startServer(){
        //Keeping the server running

        try {

            if (!serverSocket.isClosed()) {
                //If the serverSocket is opened
                do {
                    System.out.println("Server ready - Server running ");
                    //Since the server is running a message saying that the server ready -- Server running will be display
                    Socket socket = serverSocket.accept();
                    //WHen a client does connect a socket object will return
                    String adress = socket.getInetAddress().getHostAddress();
                    //To obtain the IP adress of the client socket and than converting it into a string
                    System.out.println("A Client has entered the chat with a IP Adress of " + adress);
                    //Display that a new Client has entered the chat and shows its ip adress
                    ClientHandler clientHandler = new ClientHandler(socket);
                    //Each object wil be run through the ClientHandler class
                    Thread thread = new Thread(clientHandler);
                    //Thread is created from the clienthandler object that way multiple clients can enter the chat
                    thread.start();
                    //The thread has started
                } while (!serverSocket.isClosed());
                //You would continue the do while loop until the serverSocket is closed i.e you close the terminal
            }
        } catch (IOException e){
            System.exit(1);//If fails application quits

        }
    }


    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(2222);
        //The ServerSocket port is 2222 in which all clients must connect to the same port
        Server server = new Server(serverSocket);//
        server.startServer();
        //Creates the server object with the ServerSocket and runs the server
    }
}
