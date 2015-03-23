package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ComplexSocketServer extends WinSocket {

    public static final int SERVER_PORT = 1777;

    public static void main(String args[]) throws Exception {
        new ComplexSocketServer();
    }

    private ServerSocket servSocket;
    private Socket fromClientSocket;

    public ComplexSocketServer() throws IOException {
        super("Server!");
        servSocket = new ServerSocket(SERVER_PORT);
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
        output.append("\n" + "Waiting for a connection on " + SERVER_PORT);
        fromClientSocket = servSocket.accept();

        ObjectOutputStream oos = new ObjectOutputStream(fromClientSocket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(fromClientSocket.getInputStream());

        //ComplexCompany comp;
        Object comp;
        String print;
        while ((comp = ois.readObject()) != null) {
            print = comp.toString();
            output.append(print + "\n");
            oos.writeObject("bye bye");
            break;
        }
        oos.close();
        ois.close();
    }

}
