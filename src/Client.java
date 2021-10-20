import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.out;

public class Client {

    private Socket socket;
    // This is the socket passed from the Server Side
    private BufferedReader bReaderIn;
    //TO be able to read the data from the client
    private BufferedWriter bWriterOut;
    //To be able to send the data from the client to other client
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bWriterOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //Wrapping our byte stream to character as we want to send characters than wrapped to bWriterOut to improve efficiency
            this.bReaderIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //bReaderIn so what were are receving
            this.username = username;
        } catch (IOException e) {
            System.exit(1);
        }

    }

    public void sentChat() {
        try {
            bWriterOut.write(username);
            // The client will write their username
            bWriterOut.newLine();
            // Each chat will be a new line
            bWriterOut.flush();
            // Usrname will not be big enough to fill buffer

            Scanner scanner = new Scanner(System.in);
            //Allows client to input values
            if (socket.isConnected()) {
                do {
                    String messageToSend = scanner.nextLine();
                    //Gets the message the client has sent
                    bWriterOut.write(username + ": " + messageToSend);
                    //Writes the message with the client username infront to show other clients which client wrote the message
                    bWriterOut.newLine();
                    bWriterOut.flush();
                } while (socket.isConnected());
            }
        } catch (IOException e) {
            System.exit(1);
        }
    }

    public void readChat() {
        new Thread(() -> {
            String GroupChatMsg;
            // This thread implemements the runnable

            if (socket.isConnected()) {//When the client is still connected to the socket same as the server
                do {
                    try {
                        GroupChatMsg = bReaderIn.readLine();
                        //The rest of the members in the group chat are able to see the message
                        out.println(GroupChatMsg);
                    } catch (IOException e) {
                        System.exit(1);
                    }
                } while (socket.isConnected());
            }
        }).start();
    }


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
       out.println(" Enter your username for the group chat: ");
       //The client will be prompt with this message of asking them what their username is
       String username = scanner.nextLine();
       //Username is saved ina string variable username
       Socket socket = new Socket("localhost", 2222);
       // the host will be the same (same IP adress) as they all are running from the same pc and they are all connected to the same port 2222
       Client client = new Client(socket, username);
       client.readChat();
       // the readChat method
       client.sentChat();
       //The sentChat method
    }
}


