package routine.core;

import gui.TaskBar;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Communicator {

    private Socket socketOut, socketIn;
    private ObjectOutputStream out;
    ObjectInputStream in;

    public Communicator(String serverPort) throws IOException {
        String server = serverPort.split(":")[0];
        int port = Integer.parseInt(serverPort.split(":")[1].trim());
        socketOut = new Socket(server, port);
        out = new ObjectOutputStream(socketOut.getOutputStream());
    }

    public Communicator(int port) throws IOException {
        ServerSocket servSocket = new ServerSocket(port);
        socketIn = servSocket.accept();
        in = new ObjectInputStream(socketIn.getInputStream());
    }

    public String receive() {
        try {
            Object obj = in.readObject();
            return obj.toString();
        } catch (Exception ex) {
            TaskBar.getInstance().msgError(ex);
            ex.printStackTrace();
        }
        return "receive:exception!";
    }

    public void send(String action) {
        try {
            out.writeObject(action);
        } catch (IOException ex) {
            TaskBar.getInstance().msgError(ex);
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            if (socketOut != null) {
                socketOut.close();
            }
            if (out != null) {
                out.close();
            }
            if (socketIn != null) {
                socketIn.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (Exception ex) {
            TaskBar.getInstance().msgError(ex);
            ex.printStackTrace();
        }
    }
}
