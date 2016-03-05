package feuyeux.io.bio.tcp.callback;

import feuyeux.io.common.ENV;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class TCPClient {
    private static final Logger logger = LogManager.getLogger(TCPClient.class);
    private TCPConnection conn;

    public void startClient(String remoteIP, int remotePort) {
        try {
            System.out.println("TCP BIOUDPClient..., Connecting to server " + remoteIP + ": " + remotePort);
            Socket socket = new Socket(remoteIP, remotePort);
            conn = new TCPConnection(socket);
            conn.registerConnListener(new EventHandler("TCP-BIOUDPClient"));
            conn.handleConnect();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void talk(String message) throws IOException {
        conn.sendMessage(message);
    }

    public void close() throws IOException {
        conn.close();
    }

    public static void main(String[] args) {
        TCPClient client = new TCPClient();
        client.startClient(ENV.SERVER_IP, ENV.BIO_TCP_PORT);
    }
}
