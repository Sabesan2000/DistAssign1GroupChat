import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientHandler  implements Runnable{

    public static ArrayList<ClientHandler> Clients = new ArrayList<>();
    //Keeps track of all clients and and loops the the arraylist so that we can send each message to the client
    private Socket socket;
    // This is the socket passed from the Server Side
    private BufferedReader bReaderIn;
    //TO be able to read the data from the client
    private BufferedWriter bWriterOut;
    //To be able to send the data from the client to other client
    public String clientUsername;


    public ClientHandler(Socket socket) {
        try {
            this.socket =socket;
            this.bWriterOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //Wrapping our byte stream to character as we want to send characters than wrapped to bWriterOut to improve efficiency
            this.bReaderIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //bReaderIn so what were are receving
            this.clientUsername = bReaderIn.readLine();
            //On the client side the client will be asked to input their username in which in will be read by using bReader.in.readLine()
            Clients.add(this);
            //Adds Clients to the ArrayList Client
            showMessage("Another user has entered the chat");
            //On the Server side it will display that a another user has entered the chat after the client has sucessfully entered their username
        } catch (IOException e) {
            System.exit(1);

        }
    }

    @Override
    public void run() {
        String chat;//Created a string variable that will hold the chat the client sent

        while (true) {
            if (!socket.isConnected()) break;
            try {
                chat = bReaderIn.readLine();
                //The prgoram will read and hold until a chat is sent
                showMessage(chat);
                //Display the chat to all clients
            } catch (IOException e) {
                System.exit(1);
                break;
            }
        }
    }
    public void showMessage(String messageToSend) {
        for (Iterator<ClientHandler> iterator = Clients.iterator(); iterator.hasNext(); ) {
            ClientHandler clientHandler = iterator.next();
            //Represent each clientHandler at each time during each iteration
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    //The message will broadcast to everyone except the client that created it
                    clientHandler.bWriterOut.write(messageToSend);
                    clientHandler.bWriterOut.newLine();
                    clientHandler.bWriterOut.flush();
                }
            } catch (IOException e) {
                System.exit(1);
                break;
            }
        }
    }


}
