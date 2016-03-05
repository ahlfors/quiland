package feuyeux.io.netty.udp.cmd2.controller;

import feuyeux.io.netty.udp.cmd2.entity.CommandType;
import feuyeux.io.netty.udp.cmd2.info.ControlInfo;

/**
 * Created by erichan on 2/10/14.
 */
public interface CmdController {
    CommandType getType();

    void process(ControlInfo controlInfo);
}
