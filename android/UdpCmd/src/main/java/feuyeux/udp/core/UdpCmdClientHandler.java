package feuyeux.udp.core;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UdpCmdClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private final UdpCmdClientContext context;

    public UdpCmdClientHandler(UdpCmdClientContext context) {
        this.context = context;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket datagramPacket) throws Exception {
        //logger.debug("DatagramPacket:{}", datagramPacket);
        ByteBuf content = datagramPacket.content();
        InetSocketAddress serverHost = datagramPacket.sender();
        String serverIp = serverHost.getAddress().getHostAddress();
        context.addServer(serverIp);
        logger.log(Level.FINE, "Server[" + serverHost.getHostName() + "-" + serverIp + "] response:'" + content.toString(CharsetUtil.UTF_8) + "'");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.log(Level.SEVERE, "Unexpected exception:", cause);
        //ctx.close();
    }
}
