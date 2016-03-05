package feuyeux.io.nio.udp.protocol;

public class StringInputMessage extends BaseMessage {

    private String value;

    public StringInputMessage() {
        cmdType = CMD_TYPE_STRING_INPUT;
    }

    public StringInputMessage(String value) {
        cmdType = CMD_TYPE_STRING_INPUT;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "[StringInput] TYPE=" + cmdType + ",VALUE=" + value;
    }
}
