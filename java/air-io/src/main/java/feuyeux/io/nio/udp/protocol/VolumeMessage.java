package feuyeux.io.nio.udp.protocol;

public class VolumeMessage extends BaseMessage {
    private int volume;

    public VolumeMessage() {
        cmdType = CMD_TYPE_QUERY_VOLUME;
    }

    public VolumeMessage(int volume) {
        cmdType = CMD_TYPE_QUERY_VOLUME;
        this.volume = volume;
    }

    public int volumeVal() {
        return volume;
    }

    @Override
    public String toString() {
        return "[QueryVolume] TYPE=" + cmdType + ",VOLUME=" + volume;
    }
}
