package socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ReaderSck {

    public static void main(String[] argv) throws Exception {
        int port = 1777;
        ServerSocket srv = new ServerSocket(port);

        // Wait for connection from client.
        Socket socket = srv.accept();
        BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String str;
        while ((str = rd.readLine()) != null) {
            System.out.println(str);
        }
        rd.close();
    }
}
