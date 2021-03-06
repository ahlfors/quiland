package feuyeux.udp.info;

/**
 * Created by erichan on 2/10/14.
 */
public class KeyControlInfo implements ControlInfo {
    private static final long serialVersionUID = -4730673760806254972L;
    private String keyPress;

    public KeyControlInfo() {
    }

    public KeyControlInfo(String keyPress) {
        this.keyPress = keyPress;
    }

    public String getKeyPress() {
        return keyPress;
    }

    public void setKeyPress(String keyPress) {
        this.keyPress = keyPress;
    }

    @Override
    public String toString() {
        return getKeyPress();
    }
}
