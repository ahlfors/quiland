package feuyeux.io.nio.udp.protocol;

public class KeyInputMessage extends BaseMessage {
    public static final int ACTION_DOWN = 0;
    public static final int ACTION_UP = 1;
    public static final int ACTION_PRESSED = 2;
    /*
     * Comply with standard Android key code definitions
     */
    private int keyCode;

    /*
     * 0=down, 1=up, 2=pressed(up and down)
     */
    private int keyAction;

    public KeyInputMessage(int code, int action) {
        cmdType = CMD_TYPE_KEY_INPUT;
        keyCode = code;
        keyAction = action;
    }

    public int code() {
        return keyCode;
    }

    public int action() {
        return keyAction;
    }

    @Override
    public String toString() {
        return "[KeyInput] TYPE=" + cmdType + ",KEY-CODE=" + keyCode + ",KEY-ACTION=" + keyAction;
    }
}
