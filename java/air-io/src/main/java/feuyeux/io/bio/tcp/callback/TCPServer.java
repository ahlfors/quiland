package feuyeux.io.bio.tcp.callback;

import feuyeux.io.common.ENV;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class TCPServer {
    private static final Logger logger = LogManager.getLogger(TCPServer.class);
    volatile boolean stop = false;
    HashSet<TCPConnection> conns = new HashSet<>();

    public void startServer(int localPort) {
        try {
            ServerSocket serverSocket = new ServerSocket(localPort);
            System.out.println("TCP BIOUDPServer... on port " + localPort);
            while (!stop) {
                Socket socket = serverSocket.accept();
                TCPConnection conn = new TCPConnection(socket);
                conn.registerConnListener(new EventHandler("TCP-BIOUDPServer"));
                conn.handleConnect();
                conns.add(conn);
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void stop() throws InterruptedException, IOException {
        stop = true;
        for (TCPConnection conn : conns) {
            conn.close();
        }
    }

    public static void main(String[] args) {
        TCPServer server = new TCPServer();
        server.startServer(ENV.BIO_TCP_PORT);
    }
}
