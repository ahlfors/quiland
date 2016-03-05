package feuyeux.io.nio.udp;

import feuyeux.io.common.ENV;
import feuyeux.io.common.Tooling;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * Created by feuyeux@gmail.com
 * Date: Mar 04 2014
 * Time: 1:35 PM
 */
public class NioUdpBroadCastClient {
    InetSocketAddress hostAddress = new InetSocketAddress(ENV.BROAD_CAST_IP, ENV.NIO_UDP_PORT);
    InetSocketAddress inetSocketAddress = new InetSocketAddress(Tooling.getAddress(), ENV.NIO_UDP_PORT);
    int TIMES = 10;

    public static void main(String[] args) throws Exception {
        Set<SocketAddress> servers = new NioUdpBroadCastClient().broadCast(10000);
        for (SocketAddress server : servers) {
            System.out.println(server);
        }
    }

    private Set<SocketAddress> broadCast(long waitingTime) throws IOException, InterruptedException {
        try (DatagramChannel channel = DatagramChannel.open(); Selector selector = Selector.open();) {
            channel.socket().setBroadcast(true);
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
            channel.send(getBuffer(), hostAddress);
            channel.send(getBuffer(), inetSocketAddress);

            selector.select(1000);
            Set<SocketAddress> addressSet = new HashSet<SocketAddress>();
            while (TIMES > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey key : selectionKeys) {
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        DatagramChannel dc = (DatagramChannel) key.channel();
                        SocketAddress serverAddress = dc.receive(buffer);
                        addressSet.add(serverAddress);
                    }
                }
                Thread.currentThread().sleep(waitingTime / TIMES);
                TIMES--;
            }
            return addressSet;
        }
    }

    private static ByteBuffer getBuffer() throws CharacterCodingException {
        return Charset.forName("UTF-8").newEncoder().encode(CharBuffer.wrap("hello"));
    }
}