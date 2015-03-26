package routine.core;

import gui.TaskBar;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Communicator {

    public static final String PORT = "9876";

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // Client
    public Communicator(String serverPort) throws IOException {
        String server = serverPort.split(":")[0];
        int port = Integer.parseInt(serverPort.split(":")[1].trim());
        socket = new Socket(server, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    // Server
    public Communicator(int port) throws IOException {
        ServerSocket servSocket = new ServerSocket(port);
        servSocket.setSoTimeout(30000);
        socket = servSocket.accept();
        out = new ObjectOutputStream(socket.getOutputStream());        
        in = new ObjectInputStream(socket.getInputStream());
    }

    public Object receive() {
        try {
            return in.readObject();
        } catch (Exception ex) {
            TaskBar.getInstance().msgError(ex);
            ex.printStackTrace();
        }
        return "receive:exception!";
    }

    public void send(Object obj) {
        try {
            out.writeObject(obj);
        } catch (IOException ex) {
            TaskBar.getInstance().msgError(ex);
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception ex) {
            TaskBar.getInstance().msgError(ex);
            ex.printStackTrace();
        }
    }
}
