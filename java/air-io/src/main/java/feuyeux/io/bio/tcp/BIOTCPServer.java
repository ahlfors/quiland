package feuyeux.io.bio.tcp;

import feuyeux.io.common.ENV;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOTCPServer {
    private static final Logger logger = LogManager.getLogger(BIOTCPServer.class);

    final ServerSocket serverSocket;
    final int port;
    private final ExecutorService executorService;
    volatile boolean stop = false;

    public BIOTCPServer(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(this.port);
        executorService = Executors.newFixedThreadPool(ENV.FIX_THREAD_NUMBER);
    }

    public void initialize() throws IOException {
        logger.info("Server is startup.");
        try {
            while (!stop) {
                //blocking until client connected
                Socket socket = serverSocket.accept();
                logger.info("the remote port connected: " + socket.getPort());
                try {
                    Runnable r = new TCPServerThread(socket);
                    executorService.execute(r);
                } catch (Exception ignored) {
                    socket.close();
                }
            }
            logger.info("Server is shutdown.");
        } catch (IOException e) {
            logger.error(e);
        } finally {
            serverSocket.close();
        }
    }

    public void stop() throws InterruptedException {
        stop = true;
        executorService.shutdownNow();
    }

    public static void main(String[] args) throws Exception {
        final BIOTCPServer server = new BIOTCPServer(ENV.BIO_TCP_PORT);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.initialize();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }).start();

        Thread.sleep(1000);
        server.stop();
    }
}
