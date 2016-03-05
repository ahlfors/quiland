package feuyeux.udp.controller;

import feuyeux.udp.entity.CommandType;

/**
 * Created by erichan on 2/10/14.
 */
public final class ControllerFactory {
    private ControllerFactory() {
    }

    public static CmdController getController(CommandType controlType) {
        switch (controlType) {
            case KEY: {
                return new KeyController();
            }
            case MOUSE: {
                return new MouseController();
            }
            case REMOTE_CONTROL: {
                return new RemoteControlController();
            }
            case TOUCH_PAD: {
                return new TouchPadController();
            }
            case JOY_PAD: {
                return new JoyPadController();
            }
            case VOLUME: {
                return new VolumeController();
            }
            default: {
                return null;
            }
        }
    }
}
