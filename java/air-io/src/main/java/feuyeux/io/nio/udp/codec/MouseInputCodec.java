package feuyeux.io.nio.udp.codec;

import feuyeux.io.nio.udp.protocol.BaseMessage;
import feuyeux.io.nio.udp.protocol.MouseInputMessage;

import java.nio.ByteBuffer;


public class MouseInputCodec implements MessageCodec {

    @Override
    public BaseMessage decode(byte[] buf) {
        ByteBuffer buffer = ByteBuffer.wrap(buf);
        int action = buffer.getInt();
        int arg1 = buffer.getInt();
        int arg2 = buffer.getInt();
        return new MouseInputMessage(action, arg1, arg2);
    }

    @Override
    public byte[] encode(BaseMessage command) {
        MouseInputMessage mouseCommand = (MouseInputMessage) command;
        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putInt(mouseCommand.action());
        buffer.putInt(mouseCommand.arg1());
        buffer.putInt(mouseCommand.arg2());
        return buffer.array();
    }

    public static void main(String[] args) {
        MouseInputMessage message=new MouseInputMessage(1,2,3);
        MouseInputCodec p=new MouseInputCodec();
        System.out.println(p.decode(p.encode(message)));
    }
}
