package feuyeux.udp.core;

import feuyeux.udp.ENV;
import feuyeux.udp.controller.CmdController;
import feuyeux.udp.controller.ControllerFactory;
import feuyeux.udp.entity.CommandType;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UdpCmdServer {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private EventLoopGroup group;
    private final int nettyPort;
    private LinkedBlockingQueue<CmdController> listeners;
    public static final int LISTENER_NUMBER = 64;

    public UdpCmdServer() throws InterruptedException {
        nettyPort = ENV.NETTY_PORT;
        nativeRegister();
    }

    public UdpCmdServer(int nettyPort) throws InterruptedException {
        this.nettyPort = nettyPort;
        nativeRegister();
    }

    public void init() throws InterruptedException {
        init0(true);
    }

    void init(boolean durable) throws InterruptedException {
        init0(durable);
    }

    private void init0(boolean durable) throws InterruptedException {
        group = new NioEventLoopGroup();
        final UdpCmdServerHandler udpCmdServerHandler = new UdpCmdServerHandler();
        udpCmdServerHandler.setUdpCmdServer(this);
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        protected void initChannel(DatagramChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    // new LoggingHandler(LogLevel.INFO),
                                    udpCmdServerHandler);
                        }
                    });
            ChannelFuture f = b.bind(nettyPort).sync();
            logger.log(Level.FINE, "UDP Command Server launched.");

            if (durable) {
                f.channel().closeFuture().sync();
            } else {
                if (!f.channel().closeFuture().await(ENV.CONNECT_TIMEOUT)) {
                    logger.log(Level.INFO, "UDP Command Server closed.");
                }
            }
        } finally {
            group.shutdownGracefully();
        }
    }

    public void close() {
        listeners.clear();
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    public void register(CmdController cmdController) throws InterruptedException {
        listeners.put(cmdController);
    }

    public boolean unRegister(CmdController cmdController) {
        return listeners.remove(cmdController);
    }

    public ArrayList<CmdController> getListeners(CommandType cmdType) {
        ArrayList<CmdController> result = new ArrayList<>();
        if (listeners.iterator().hasNext()) {
            CmdController cmdController = listeners.iterator().next();
            if (cmdController.getType().equals(cmdType)) {
                result.add(cmdController);
            }
        }
        return result;
    }

    private void nativeRegister() throws InterruptedException {
        listeners = new LinkedBlockingQueue<>(LISTENER_NUMBER);
        register(ControllerFactory.getController(CommandType.KEY));
        register(ControllerFactory.getController(CommandType.MOUSE));
        register(ControllerFactory.getController(CommandType.REMOTE_CONTROL));
        register(ControllerFactory.getController(CommandType.TOUCH_PAD));
        register(ControllerFactory.getController(CommandType.JOY_PAD));
        register(ControllerFactory.getController(CommandType.VOLUME));
    }
}
