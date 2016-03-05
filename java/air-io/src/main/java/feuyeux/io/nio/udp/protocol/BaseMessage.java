package feuyeux.io.nio.udp.protocol;

public class BaseMessage {

    public static final int CMD_TYPE_INVALID = 0;
    public static final int CMD_TYPE_KEY_INPUT = 1000;
    public static final int CMD_TYPE_MOUSE_INPUT = 2000;
    public static final int CMD_TYPE_STRING_INPUT = 3000;

    public static final int CMD_TYPE_QUERY_ONLINE = 8000;
    public static final int CMD_TYPE_QUERY_VOLUME = 8001;

    protected int cmdType = 0;

    public int getType() {
        return cmdType;
    }

    @Override
    public String toString() {
        return "TYPE=" + cmdType;
    }
}
