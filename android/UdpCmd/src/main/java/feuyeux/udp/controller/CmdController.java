package feuyeux.udp.controller;

import feuyeux.udp.entity.CommandType;
import feuyeux.udp.info.ControlInfo;

/**
 * Created by erichan on 2/10/14.
 */
public interface CmdController {
    CommandType getType();
    void process(ControlInfo controlInfo);
}
