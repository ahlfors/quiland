package feuyeux.io.bio.tcp;

import feuyeux.io.bio.tcp.callback.TCPClient;
import feuyeux.io.bio.tcp.callback.TCPServer;
import feuyeux.io.common.ENV;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class TCPCommunicationTest {
    private static final Logger logger = LogManager.getLogger(TCPCommunicationTest.class);
    final String message = "tcp-bio-test";

    @Test
    public void testTcpCommunication0() throws IOException, InterruptedException {
        final TCPServer server = new TCPServer();
        final TCPClient client = new TCPClient();

        new Thread() {
            @Override
            public void run() {
                server.startServer(ENV.BIO_TCP_PORT);
            }
        }.start();
        client.startClient(ENV.SERVER_IP, ENV.BIO_TCP_PORT);
        client.talk(message);
        Thread.sleep(100);
        try {
            server.stop();
        } catch (Exception e) {
            logger.error("stop server failed.", e);
        }
        try {
            client.talk(message);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Test
    public void testTcpCommunication() throws IOException, InterruptedException, ExecutionException {

        final BIOTCPServer server = new BIOTCPServer(ENV.BIO_TCP_PORT);
        final BIOTCPClient client = new BIOTCPClient(ENV.SERVER_IP, ENV.BIO_TCP_PORT);
        ExecutorService e = Executors.newFixedThreadPool(1);
        e.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    server.initialize();
                } catch (Exception e) {
                    logger.info(e);
                }
            }
        });

        FutureTask<String> f = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return client.sendMessage(message);
            }
        });
        f.run();

        String received = f.get();
        logger.info(received);
        Assert.assertTrue(received.contains(message));

        server.stop();
        client.sendMessage("tip for break server thread's reading block");

        final BIOTCPClient client2 = new BIOTCPClient(ENV.SERVER_IP, ENV.BIO_TCP_PORT);
        received = client2.sendMessage(message);
        logger.info(received);
        Assert.assertNull(received);

        received = client.sendMessage(message);
        logger.info(received);
        Assert.assertNull(received);
    }
}