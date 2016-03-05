package feuyeux.udp.codec;

import feuyeux.udp.info.ControlInfo;

/**
 * Created by erichan on 2/10/14.
 */
public interface ControlInfoCodec {

    byte[] encode(ControlInfo controlInfo);

    ControlInfo decode(byte[] buf);
}
