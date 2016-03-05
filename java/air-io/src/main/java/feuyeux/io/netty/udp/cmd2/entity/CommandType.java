package feuyeux.io.netty.udp.cmd2.entity;

/**
 * Created by erichan on 2/10/14.
 */
public enum CommandType {
    KEY(0), MOUSE(1), REMOTE_CONTROL(2), TOUCH_PAD(3), JOY_PAD(4), VOLUME(5), Unknown(-1);


    private final int value;

    CommandType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CommandType getInstance(int controlType) {
        switch (controlType) {
            case 0:
                return KEY;
            case 1:
                return MOUSE;
            case 2:
                return REMOTE_CONTROL;
            case 3:
                return TOUCH_PAD;
            case 4:
                return JOY_PAD;
            case 5:
                return VOLUME;
            default:
                return Unknown;
        }
    }
}