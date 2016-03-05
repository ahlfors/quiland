package feuyeux.io.nio.udp.codec;

import feuyeux.io.nio.udp.protocol.BaseMessage;
import feuyeux.io.nio.udp.protocol.KeyInputMessage;

import java.nio.ByteBuffer;

public class KeyInputCodec implements MessageCodec {
    @Override
    public BaseMessage decode(byte[] buf) {
        ByteBuffer buffer = ByteBuffer.wrap(buf);
        int code = buffer.getInt();
        int action = buffer.getInt();
        return new KeyInputMessage(code, action);
    }

    @Override
    public byte[] encode(BaseMessage command) {
        KeyInputMessage keyCommand = (KeyInputMessage) command;
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putInt(keyCommand.code());
        buffer.putInt(keyCommand.action());
        return buffer.array();
    }
}
