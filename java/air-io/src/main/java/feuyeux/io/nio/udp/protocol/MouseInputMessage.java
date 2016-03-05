package feuyeux.io.nio.udp.protocol;

public class MouseInputMessage extends BaseMessage {

    public static final int MOUSE_ACTION_MOVE_CURSOR = 0;
    public static final int MOUSE_ACTION_BUTTON_PRESS = 1;

    /*
     * For cursor move action: arg1=dx, arg2=dy
     * For button click action: arg1 and arg2 are not checked
     */
    private int mouseAction;
    private int arg1;
    private int arg2;

    public MouseInputMessage(int action, int arg1, int arg2) {
        cmdType = CMD_TYPE_MOUSE_INPUT;
        this.mouseAction = action;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public int action() {
        return mouseAction;
    }

    public int arg1() {
        return arg1;
    }

    public int arg2() {
        return arg2;
    }

    @Override
    public String toString() {
        return "[MouseInput] TYPE=" + cmdType + ",X=" + arg1 + ",Y=" + arg2 + ",MOUSE-ACTION=" + mouseAction;
    }
}
