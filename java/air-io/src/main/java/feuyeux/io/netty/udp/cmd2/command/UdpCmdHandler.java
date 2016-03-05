package feuyeux.io.netty.udp.cmd2.command;

import feuyeux.io.netty.udp.cmd2.core.UdpCmdClient;
import feuyeux.io.netty.udp.cmd2.entity.CommandType;
import feuyeux.io.netty.udp.cmd2.entity.UdpCommand;
import feuyeux.io.netty.udp.cmd2.info.JoyPadInfo;
import feuyeux.io.netty.udp.cmd2.info.KeyControlInfo;
import feuyeux.io.netty.udp.cmd2.info.RemoteControlInfo;
import feuyeux.io.netty.udp.cmd2.info.TouchPadInfo;
import feuyeux.io.netty.udp.cmd2.info.VolumeInfo;

/**
 * Created by erichan on 2/26/14.
 */
public class UdpCmdHandler {
    final UdpCmdClient client;

    public UdpCmdHandler(String serverIp, int nettyPort) throws InterruptedException {
        this.client = UdpCmdClient.getUdpClient(serverIp, nettyPort);
    }

    public void sendKey(KeyControlInfo keyControlInfo) {
        UdpCommand keyCommand = new UdpCommand(CommandType.KEY, keyControlInfo);
        client.send(keyCommand);
    }

    public void sendRemoteControl(RemoteControlInfo remoteControlInfo) {
        UdpCommand keyCommand = new UdpCommand(CommandType.REMOTE_CONTROL, remoteControlInfo);
        client.send(keyCommand);
    }

    public void sendJoyPad(JoyPadInfo joyPadInfo) {
        UdpCommand keyCommand = new UdpCommand(CommandType.JOY_PAD, joyPadInfo);
        client.send(keyCommand);
    }

    public void sendTouchPad(TouchPadInfo touchPadInfo) {
        UdpCommand keyCommand = new UdpCommand(CommandType.TOUCH_PAD, touchPadInfo);
        client.send(keyCommand);
    }

    public void sendVolume(VolumeInfo volumeInfo) {
        UdpCommand keyCommand = new UdpCommand(CommandType.VOLUME, volumeInfo);
        client.send(keyCommand);
    }
}
