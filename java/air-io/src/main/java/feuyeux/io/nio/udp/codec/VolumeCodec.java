package feuyeux.io.nio.udp.codec;

import feuyeux.io.nio.udp.protocol.BaseMessage;
import feuyeux.io.nio.udp.protocol.VolumeMessage;

import java.nio.ByteBuffer;

public class VolumeCodec implements MessageCodec {
    @Override
    public BaseMessage decode(byte[] buf) {
        ByteBuffer buffer = ByteBuffer.wrap(buf);
        int volume = buffer.getInt();
        return new VolumeMessage(volume);
    }

    @Override
    public byte[] encode(BaseMessage command) {
        VolumeMessage volumeCommand = (VolumeMessage) command;
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(volumeCommand.volumeVal());
        return buffer.array();
    }
}
