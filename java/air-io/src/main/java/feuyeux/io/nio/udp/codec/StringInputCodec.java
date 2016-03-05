package feuyeux.io.nio.udp.codec;

import feuyeux.io.nio.udp.protocol.BaseMessage;
import feuyeux.io.nio.udp.protocol.StringInputMessage;

public class StringInputCodec implements MessageCodec {
    StringInputCodec() {
    }

    @Override
    public BaseMessage decode(byte[] buf) {
        StringInputMessage stringCommand = new StringInputMessage();
        stringCommand.setValue(new String(buf));
        return stringCommand;
    }

    @Override
    public byte[] encode(BaseMessage command) {
        StringInputMessage stringCommand = (StringInputMessage) command;
        return stringCommand.getValue().getBytes();
    }
}
