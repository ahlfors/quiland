package feuyeux.io.nio.udp;

import feuyeux.io.common.ENV;
import feuyeux.io.nio.udp.codec.CodecUtility;
import feuyeux.io.nio.udp.protocol.BaseMessage;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;

public final class NIOUDPServer implements Runnable {
    Selector selector;

    public static void main(String[] args) throws Exception {
        new NIOUDPServer().run();
    }

    @Override
    public void run() {
        try {
            DatagramChannel receiveChannel = DatagramChannel.open();
            DatagramSocket serverSocket = receiveChannel.socket();
            serverSocket.bind(new InetSocketAddress(ENV.NIO_UDP_PORT));
            System.out.println("Data receive listen on port: " + ENV.NIO_UDP_PORT);
            receiveChannel.configureBlocking(false);

            selector = Selector.open();
            receiveChannel.register(selector, SelectionKey.OP_READ);
        } catch (Exception e) {
            System.err.println(e);
        }
        while (true) {
            try {
                int keyNumber = selector.select(1000);
                if (keyNumber > 0) {
                    for (SelectionKey key : selector.selectedKeys()) {
                        if (!key.isValid()) {
                            continue;
                        }
                        if (key.isReadable()) {
                            handle(key);
//                            read(key);
//                            key.interestOps(SelectionKey.OP_WRITE);
//                        } else if (key.isWritable()) {
//                            write(key);
//                            key.interestOps(SelectionKey.OP_READ);
                        }
                    }
                    selector.selectedKeys().clear();
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    private void handle(SelectionKey key) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        DatagramChannel dc = (DatagramChannel) key.channel();
        SocketAddress sender = dc.receive(buffer);
        buffer.flip();
        BaseMessage message = CodecUtility.decode(buffer);
        System.out.println("Message from client[" + sender + "]: " + message);
        dc.send(CodecUtility.encode(message), sender);
    }

    void write(SelectionKey key) throws IOException {
        DatagramChannel dc = (DatagramChannel) key.channel();
        AttachmentPair attachmentPair = (AttachmentPair) key.attachment();
        dc.send(attachmentPair.getMessage(), attachmentPair.getAddress());
    }

    void read(SelectionKey key) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        DatagramChannel dc = (DatagramChannel) key.channel();
        SocketAddress sender = dc.receive(buffer);
        buffer.flip();
        String message = Charset.forName(ENV.UTF_8).decode(buffer).toString();
        System.out.println("Message from client[" + sender + "]: " + message);
        if (ENV.QUIT.equalsIgnoreCase(message.trim())) {
            dc.close();
            selector.close();
            System.out.println("NIOUDPServer has been shutdown!");
            System.exit(0);
        }
        String outMessage = "NIOUDPServer response：" + message;
        key.attach(new AttachmentPair(sender, Charset.forName(ENV.UTF_8).encode(outMessage)));
    }


    class AttachmentPair {
        SocketAddress address;
        ByteBuffer message;

        AttachmentPair(SocketAddress address, ByteBuffer message) {
            this.address = address;
            this.message = message;
        }

        public ByteBuffer getMessage() {
            return message;
        }

        public SocketAddress getAddress() {
            return address;
        }
    }
}
