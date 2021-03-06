package feuyeux.io.nio.tcp;

import feuyeux.io.common.ENV;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class NIOTCPServer {
    private static final Logger logger = LogManager.getLogger(NIOTCPServer.class);
    final Selector selector;
    final ServerSocket serverSocket;
    final int port;
    boolean blocking = false;

    public NIOTCPServer(int port) throws IOException {
        this.port = port;
        selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        serverSocket = ssc.socket();
        serverSocket.bind(new InetSocketAddress(this.port));
        initialize();
    }

    private void initialize() throws IOException {
        while (true) {
            final long timeout = 1000; // milliseconds
            int nKeys = selector.select(timeout);
            if (nKeys > 0) {
                for (SelectionKey key : selector.selectedKeys()) {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = server.accept();
                        if (socketChannel == null) {
                            continue;
                        }
                        logger.info("the remote port connected: " + socketChannel.socket().getPort());

                        socketChannel.configureBlocking(blocking); //blocking default value is true

                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        SocketChannel channel = (SocketChannel) key.channel();
                        int readBytes = 0;
                        String message = null;
                        try {
                            int ret;
                            try {
                                while ((ret = channel.read(buffer)) > 0) {
                                    readBytes += ret;
                                }
                            } catch (Exception e) {
                                readBytes = 0;
                                // IGNORE
                            } finally {
                                buffer.flip();
                            }
                            if (readBytes > 0) {
                                message = Charset.forName("UTF-8").decode(buffer).toString();
                                buffer = null;
                            }
                        } finally {
                            if (buffer != null) {
                                buffer.clear();
                            }
                        }
                        if (readBytes > 0) {
                            logger.info("Message from client: " + message);
                            if (message != null) {
                                message = message.trim();
                            }
                            if (ENV.QUIT.equalsIgnoreCase(message)) {
                                channel.close();
                                selector.close();
                                logger.info("BIOUDPServer has been shutdown!");
                                System.exit(0);
                            }
                            String outMessage = "BIOUDPServer response: " + message;
                            channel.write(Charset.forName("UTF-8").encode(outMessage));
                        }
                    }
                }
                selector.selectedKeys().clear();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new NIOTCPServer(ENV.NIO_TCP_PORT);
    }
}
