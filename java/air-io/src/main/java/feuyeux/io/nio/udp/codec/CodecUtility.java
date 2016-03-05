package feuyeux.io.nio.udp.codec;


import feuyeux.io.nio.udp.protocol.BaseMessage;
import feuyeux.io.nio.udp.protocol.StringInputMessage;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 14-2-10.
 */
public final class CodecUtility {
    private CodecUtility() {
    }

    public static ByteBuffer encode(BaseMessage command) {
        int cmdType = command.getType();
        byte[] bytes = CodecFactory.getCodec(cmdType).encode(command);
        ByteBuffer buffer = ByteBuffer.allocate(4 + bytes.length);
        buffer.putInt(cmdType);
        buffer.put(bytes);
        buffer.flip();
        return buffer;
    }

    public static BaseMessage decode(ByteBuffer byteBuf) {
        int type = byteBuf.getInt();
        byte[] dst = new byte[byteBuf.remaining()];
        byteBuf.get(dst);
        return CodecFactory.getCodec(type).decode(dst);
    }

    public static void main(String[] args) {
        StringInputMessage command = new StringInputMessage("1234567890");
        ByteBuffer encode = CodecUtility.encode(command);
        BaseMessage decode = CodecUtility.decode(encode);
        encode = CodecUtility.encode(decode);
        decode = CodecUtility.decode(encode);
        System.out.println(decode);
    }
}
