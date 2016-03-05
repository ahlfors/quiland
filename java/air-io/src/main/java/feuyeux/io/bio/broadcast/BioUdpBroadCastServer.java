package feuyeux.io.bio.broadcast;

import feuyeux.io.common.ENV;
import feuyeux.io.common.Tooling;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BioUdpBroadCastServer implements Runnable {
    public static BioUdpBroadCastServer getInstance() {
        return DiscoveryThreadHolder.INSTANCE;
    }

    private static class DiscoveryThreadHolder {
        private static final BioUdpBroadCastServer INSTANCE = new BioUdpBroadCastServer();
    }

    DatagramSocket socket;

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(ENV.BIO_BROADCAST_PORT, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            while (true) {
                byte[] bytes = new byte[15000];
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                socket.receive(packet);

                InetAddress clientAddress = packet.getAddress();
                byte[] clientMessage = packet.getData();
                String message = new String(clientMessage).trim();
                System.out.println(clientAddress);
                if (ENV.DISCOVERY.equals(message)) {
                    byte[] sendData = ENV.DISCOVERY.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, packet.getPort());
                    socket.send(sendPacket);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(BioUdpBroadCastServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        System.out.println("broadcasting server is launched: " + Tooling.getAddress());
        BioUdpBroadCastServer.getInstance().run();
    }
}
