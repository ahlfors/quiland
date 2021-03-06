package feuyeux.io.netty.udp.cmd2.codec;

import feuyeux.io.netty.udp.cmd2.info.ControlInfo;

/**
 * Created by erichan on 2/10/14.
 */
public interface ControlInfoCodec {

    byte[] encode(ControlInfo controlInfo);

    ControlInfo decode(byte[] buf);
}
