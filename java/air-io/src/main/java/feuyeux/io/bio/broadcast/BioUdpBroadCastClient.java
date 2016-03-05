package feuyeux.io.bio.broadcast;

import feuyeux.io.common.ENV;
import feuyeux.io.common.Tooling;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 *
 * Created by feuyeux@gmail.com
 * Date: Mar 04 2014
 * Time: 2:22 PM
 */
public class BioUdpBroadCastClient {
    private static final int TIMEOUT = 5;

    public static void main(String[] args) throws Exception {
        String[] result = BioUdpBroadCastClient.discovery(TIMEOUT);
        for (int i = 0; i < result.length; i++) {
            System.out.println("Broadcast Server:" + result[i]);
        }
    }

    public static String[] discovery(int waitingTime) throws IOException, InterruptedException {
        DatagramSocket c = new DatagramSocket();
        c.setBroadcast(true);
        byte[] sendData = ENV.DISCOVERY.getBytes();
        ArrayList<String> result = new ArrayList<String>();
        //Try the 255.255.255.255 first
        while (waitingTime > 0) {
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ENV.BROAD_CAST_IP), ENV.BIO_BROADCAST_PORT);
                c.send(sendPacket);
                String address = handleResponse(c);
                if (address != null && result.indexOf(address) < 0) {
                    result.add(address);
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
            waitingTime--;
        }

//        String address = broadcast2Address(c, sendData);
//        if (result.indexOf(address) < 0) {
//            result.add(address);
//        }
        c.close();
        return result.toArray(new String[result.size()]);
    }

    static String broadcast2Address(DatagramSocket c, byte[] sendData) {
        InetAddress broadcast = Tooling.getAddress();
        try {
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, ENV.BIO_BROADCAST_PORT);
            c.send(sendPacket);
            String address = handleResponse(c);
            if (address != null) {
                return address;
            }
        } catch (Exception e) {
        }
        return null;
    }

    private static String handleResponse(DatagramSocket c) throws IOException {
        String address = null;
        byte[] bytes = new byte[15000];
        DatagramPacket receivePacket = new DatagramPacket(bytes, bytes.length);
        c.receive(receivePacket);
        String message = new String(receivePacket.getData()).trim();
        if (ENV.DISCOVERY.equals(message)) {
            address = receivePacket.getAddress().getHostAddress();
        }
        return address;
    }
}
