package feuyeux.udp.core;

import feuyeux.udp.ENV;
import feuyeux.udp.entity.UdpCommand;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UdpCmdClient {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private static UdpCmdClient instance;
    private static UdpCmdClientContext context;

    final String serverIp;
    final int nettyPort;
    private EventLoopGroup group;
    private Channel channel;

    UdpCmdClient() {
        serverIp = ENV.SERVER_IP;
        nettyPort = ENV.NETTY_PORT;
    }

    UdpCmdClient(String serverIp, int nettyPort, int type) throws InterruptedException {
        this.serverIp = serverIp;
        this.nettyPort = nettyPort;
        group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        final UdpCmdClientHandler clientHandler = new UdpCmdClientHandler(context);

        if (type > 0) {
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        public void initChannel(DatagramChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    //new LoggingHandler(LogLevel.INFO),
                                    clientHandler
                            );
                        }
                    });
            channel = b.connect(serverIp, nettyPort).sync().channel();
        } else {
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, Boolean.TRUE)
                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        public void initChannel(DatagramChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    //new LoggingHandler(LogLevel.INFO),
                                    clientHandler
                            );
                        }
                    });
            channel = b.bind(new InetSocketAddress(0)).sync().channel();
        }
    }

    public static synchronized UdpCmdClient getUdpClient(String serverIp, int nettyPort) throws InterruptedException {
        if (serverIp == null || serverIp.isEmpty()) {
            return null;
        }
        if (instance == null) {
            if (context == null) {
                context = new UdpCmdClientContext();
            }
            instance = new UdpCmdClient(serverIp, nettyPort, 1);
        } else {
            if (!instance.serverIp.equals(serverIp) || instance.nettyPort != nettyPort) {
                instance.stop();
                instance = new UdpCmdClient(serverIp, nettyPort, 1);
            }
        }
        return instance;
    }

    public static synchronized UdpCmdClient getBroadcastClient(int nettyPort) throws InterruptedException {
        if (instance == null) {
            context = new UdpCmdClientContext();
            instance = new UdpCmdClient(null, nettyPort, 0);
        }
        return instance;
    }

    public static synchronized void clean() {
        instance.stop();
        instance = null;
    }

    public void broadcast(String message) {
        write(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8), message);
    }

    public void send(UdpCommand udpCommand) {
        ByteBuf data = UdpCmdCodec.encode(udpCommand);
        write(data, udpCommand.toString());
    }

    private void write(ByteBuf data, String description) {
        final String receiver = serverIp == null ? "255.255.255.255" : serverIp;
        DatagramPacket udpPacket = new DatagramPacket(data, new InetSocketAddress(receiver, nettyPort));
        ChannelFuture cf = channel.writeAndFlush(udpPacket);
        logger.log(Level.FINE, "UDP Command[" + description + "] has been send.");
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    logger.log(Level.FINE, "UDP Command has been received on {}.", receiver);
                } else {
                    logger.log(Level.WARNING, "UDP Command send to " + receiver + " during the transports", future.cause());
                }
            }
        });
    }

    public String[] getServers(long timeout, TimeUnit unit) throws InterruptedException {
        String[] servers = context.getServers(timeout, unit);
        if (servers == null) {
            return new String[] { "" };
        } else {
            return servers;
        }
    }

    void stop() {
        group.shutdownGracefully();
    }
}
