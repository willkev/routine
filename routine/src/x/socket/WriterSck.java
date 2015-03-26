package x.socket;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WriterSck {

    public static void main(String[] argv) throws Exception {
        int port = 1777;
        ServerSocket srv = new ServerSocket(port);

        // Wait for connection from client.
        Socket socket = srv.accept();
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        wr.write("aString");
        wr.flush();
    }
}
