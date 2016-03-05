package feuyeux.udp.controller;

import feuyeux.udp.entity.CommandType;
import feuyeux.udp.info.ControlInfo;
import feuyeux.udp.info.KeyControlInfo;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by erichan on 2/10/14.
 */
public class VolumeController implements CmdController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    public VolumeController() {

    }

    @Override
    public CommandType getType() {
        return CommandType.KEY;
    }

    @Override
    public void process(ControlInfo controlInfo) {
        KeyControlInfo keyControlInfo = (KeyControlInfo) controlInfo;
        logger.log(Level.FINE, "Volume Press is: " + keyControlInfo.getKeyPress());
        //......
    }
}
