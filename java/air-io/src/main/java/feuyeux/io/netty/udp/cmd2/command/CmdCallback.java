package feuyeux.io.netty.udp.cmd2.command;

import feuyeux.io.netty.udp.cmd2.info.ControlInfo;

/**
 * Created by erichan on 2/26/14.
 */
public interface CmdCallback {
    void process(ControlInfo controlInfo);
}
