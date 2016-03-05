package feuyeux.io.nio.udp;

import feuyeux.io.common.ENV;
import feuyeux.io.nio.udp.codec.CodecUtility;
import feuyeux.io.nio.udp.protocol.BaseMessage;
import feuyeux.io.nio.udp.protocol.StringInputMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class NIOUDPClient implements Runnable {
    private LinkedBlockingQueue<BaseMessage> commandQueue;
    public static final int TIMEOUT = 1000;
    private static final int QUEUE_CAPACITY = 1024;

    public static void main(String[] args) throws Exception {
        final NIOUDPClient nioudpClient = new NIOUDPClient();
        new Thread("Client Thread") {
            @Override
            public void run() {
                nioudpClient.run();
            }
        }.start();

        while (true) {
            nioudpClient.send(new StringInputMessage("1"));
            Thread.sleep(1000);
        }
    }

    NIOUDPClient() {
        commandQueue = new LinkedBlockingQueue<BaseMessage>(QUEUE_CAPACITY);
    }

    public void send(BaseMessage message) {
        commandQueue.offer(message);
    }

    @Override
    public void run() {
        try {
            DatagramChannel sendChannel = DatagramChannel.open();
            sendChannel.configureBlocking(false);
            SocketAddress target = new InetSocketAddress(ENV.SERVER_IP, ENV.NIO_UDP_PORT);
            sendChannel.connect(target);

            Selector selector = Selector.open();
            sendChannel.register(selector, SelectionKey.OP_READ);

            while (true) {
                //handleConsole(sendChannel, selector);
                BaseMessage command = commandQueue.poll(50, TimeUnit.MILLISECONDS);
                if (command != null) {
                    sendChannel.write(CodecUtility.encode(command));
                }
                if (selector.select(TIMEOUT) > 0) {
                    for (SelectionKey key : selector.selectedKeys()) {
                        if (!key.isValid()) {
                            continue;
                        }
                        if (key.isReadable()) {
                            read(key);
                        } else if (key.isWritable()) {

                        }
                    }
                    selector.selectedKeys().clear();
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    void handleConsole(DatagramChannel sendChannel, Selector selector) throws IOException {
        BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
        String command = systemIn.readLine();
        if (command == null || ENV.QUIT.equalsIgnoreCase(command.trim())) {
            systemIn.close();
            sendChannel.close();
            selector.close();
            System.out.println("NIOUDPClient quit!");
            System.exit(0);
        }
        sendChannel.write(Charset.forName(ENV.UTF_8).encode(command));
    }

    private void read(SelectionKey key) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        DatagramChannel dc = (DatagramChannel) key.channel();
        dc.receive(buffer);
        buffer.flip();
        System.out.println(Charset.forName(ENV.UTF_8).decode(buffer));
    }
}
