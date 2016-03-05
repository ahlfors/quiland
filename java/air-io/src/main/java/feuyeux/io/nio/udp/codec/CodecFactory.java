package feuyeux.io.nio.udp.codec;

import java.util.HashMap;

import static feuyeux.io.nio.udp.protocol.BaseMessage.*;

public final class CodecFactory {
    private static HashMap<Integer, MessageCodec> cmdMap;

    static {
        cmdMap = new HashMap<Integer, MessageCodec>();
        cmdMap.put(CMD_TYPE_KEY_INPUT, new KeyInputCodec());
        cmdMap.put(CMD_TYPE_MOUSE_INPUT, new MouseInputCodec());
        cmdMap.put(CMD_TYPE_STRING_INPUT, new StringInputCodec());
        cmdMap.put(CMD_TYPE_QUERY_VOLUME, new VolumeCodec());
    }

    private CodecFactory() {
    }

    public static MessageCodec getCodec(int type) {
        return cmdMap.get(type);
    }
}
