package feuyeux.io.nio.udp.codec;

import feuyeux.io.nio.udp.protocol.BaseMessage;

/**
 * Created by erichan on 2/10/14.
 */
public interface MessageCodec {

    byte[] encode(BaseMessage command);

    BaseMessage decode(byte[] buf);
}
