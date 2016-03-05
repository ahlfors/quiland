package feuyeux.udp.core;

import feuyeux.udp.codec.ControlInfoCodecFactory;
import feuyeux.udp.entity.CommandType;
import feuyeux.udp.entity.UdpCommand;
import feuyeux.udp.info.ControlInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by Administrator on 14-2-10.
 */
public final class UdpCmdCodec {
    private UdpCmdCodec() {
    }

    public static ByteBuf encode(UdpCommand udpCommand) {
        ByteBuf byteBuf = Unpooled.buffer();
        CommandType commandType = udpCommand.getType();
        byteBuf.writeInt(commandType.ordinal());
        byteBuf.writeBytes(ControlInfoCodecFactory.getCodec(commandType).encode(udpCommand.getControlInfo()));
        return byteBuf;
    }

    public static UdpCommand decode(ByteBuf byteBuf) {
        int type = byteBuf.readInt();
        int currentIndex = byteBuf.readerIndex();
        int endIndex = byteBuf.writerIndex();

        byte[] dst = new byte[endIndex - currentIndex];
        byteBuf.readBytes(dst);
        ControlInfo controlInfo = ControlInfoCodecFactory.getCodec(type).decode(dst);
        return new UdpCommand(CommandType.getInstance(type), controlInfo);
    }
}
