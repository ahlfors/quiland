package feuyeux.udp.command;

import feuyeux.udp.info.ControlInfo;

/**
 * Created by erichan on 2/26/14.
 */
public interface CmdCallback {
    void process(ControlInfo controlInfo);
}
