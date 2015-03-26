package x.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ComplexSocketServer extends WinSocket {

    public static final int PORT = 9876;

    public static void main(String args[]) throws Exception {
        new ComplexSocketServer();
    }

    private ServerSocket servSocket;
    private Socket fromClientSocket;

    public ComplexSocketServer() throws IOException {
        super("Server!");
        servSocket = new ServerSocket(PORT);
        while (true) {
            try {
                connecting();
            } catch (Exception ex) {
                ex.printStackTrace();
                fromClientSocket.close();
            }
        }
    }

    private void connecting() throws Exception {
        output2.append("Waiting for a connection on " + PORT);
        fromClientSocket = servSocket.accept();
        output2.append("\n ---Accepted!");

        ObjectOutputStream oos = new ObjectOutputStream(fromClientSocket.getOutputStream());
        output2.append("\n ---get OUT Stream!");

        ObjectInputStream ois = new ObjectInputStream(fromClientSocket.getInputStream());
        output2.append("\n ---get IN Stream!");

        //ComplexCompany comp;
        Object comp;
        while ((comp = ois.readObject()) != null) {
            output2.append(comp.toString() + "\n");
            oos.writeObject("bye bye");
            output2.append("\n ---writeObject feedback!");
            break;
        }
        output2.append("\n");
        oos.close();
        ois.close();
    }

}
