package feuyeux.udp.codec;

import feuyeux.udp.info.ControlInfo;
import feuyeux.udp.info.KeyControlInfo;

/**
 * Created by erichan on 2/10/14.
 */
public class KeyControlInfoCodec implements ControlInfoCodec {
    KeyControlInfoCodec() {
    }

    @Override
    public byte[] encode(ControlInfo controlInfo) {
        KeyControlInfo keyControlInfo = (KeyControlInfo) controlInfo;
        return keyControlInfo.getKeyPress().getBytes();
    }

    @Override
    public ControlInfo decode(byte[] buf) {
        KeyControlInfo keyControlInfo = new KeyControlInfo();
        keyControlInfo.setKeyPress(new String(buf));
        return keyControlInfo;
    }
}
