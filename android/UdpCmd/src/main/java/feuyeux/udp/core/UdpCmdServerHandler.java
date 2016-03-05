package feuyeux.udp.core;

import feuyeux.udp.controller.CmdController;
import feuyeux.udp.entity.UdpCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UdpCmdServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private UdpCmdServer udpCmdServer;

    public void setUdpCmdServer(UdpCmdServer udpCmdServer) {
        this.udpCmdServer = udpCmdServer;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket datagramPacket) throws Exception {
        ByteBuf content = datagramPacket.content();
        InetSocketAddress sender = datagramPacket.sender();

        UdpCommand udpCommand = null;

        try {
            udpCommand = UdpCmdCodec.decode(content);
        } catch (Exception ignored) {
        }
        if (udpCommand != null) {
            //logger.debug("DatagramPacket:{}", datagramPacket);
            logger.log(Level.FINE, "Client[" + sender + "] request:'" + udpCommand + "'");

            ArrayList<CmdController> cmdControllers = udpCmdServer.getListeners(udpCommand.getType());
            for (CmdController controller : cmdControllers) {
                controller.process(udpCommand.getControlInfo());
            }
            ByteBuf responseMessage = Unpooled.copiedBuffer("handled:" + udpCommand, CharsetUtil.UTF_8);
            ctx.write(new DatagramPacket(responseMessage, sender));
        } else {
            content.resetReaderIndex();
            String message = content.toString(CharsetUtil.UTF_8);
            logger.log(Level.FINE, "Client[" + sender + "] request:'" + message + "'");
            ByteBuf responseMessage = Unpooled.copiedBuffer("handled:" + message, CharsetUtil.UTF_8);
            ctx.write(new DatagramPacket(responseMessage, sender));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        logger.log(Level.WARNING, "Unexpected exception from downstream.", cause);
        ctx.close();
    }
}
