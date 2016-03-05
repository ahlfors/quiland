package feuyeux.io.netty.udp.cmd2.core;

import feuyeux.io.netty.udp.cmd2.controller.CmdController;
import feuyeux.io.netty.udp.cmd2.entity.UdpCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class UdpCmdServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private static final Logger logger = LogManager.getLogger(UdpCmdServerHandler.class.getName());
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
            logger.debug("Client[{}] request:'{}'", sender, udpCommand);

            ArrayList<CmdController> cmdControllers = udpCmdServer.getListeners(udpCommand.getType());
            for (CmdController controller : cmdControllers) {
                controller.process(udpCommand.getControlInfo());
            }
            ByteBuf responseMessage = Unpooled.copiedBuffer("handled:" + udpCommand, CharsetUtil.UTF_8);
            ctx.write(new DatagramPacket(responseMessage, sender));
        } else {
            content.resetReaderIndex();
            String message = content.toString(CharsetUtil.UTF_8);
            logger.debug("Client[{}] request:'{}'", sender, message);
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
        logger.warn("Unexpected exception from downstream.", cause);
        ctx.close();
    }
}
