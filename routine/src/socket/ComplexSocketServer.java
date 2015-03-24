package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ComplexSocketServer extends WinSocket {

    public static final int PORT = 1777;

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
            }
        }
        //fromClientSocket.close();
    }

    private void connecting() throws Exception {
        output.append("\n" + "Waiting for a connection on " + PORT);
        fromClientSocket = servSocket.accept();
        output.append("\n ---Accepted!");

        ObjectOutputStream oos = new ObjectOutputStream(fromClientSocket.getOutputStream());
        output.append("\n ---get OUT Stream!");

        ObjectInputStream ois = new ObjectInputStream(fromClientSocket.getInputStream());
        output.append("\n ---get IN Stream!");

        //ComplexCompany comp;
        Object comp;
        while ((comp = ois.readObject()) != null) {
            output.append(comp.toString() + "\n");
            oos.writeObject("bye bye");
            output.append("\n ---writeObject feedback!");
            break;
        }
        oos.close();
        ois.close();
    }

}
