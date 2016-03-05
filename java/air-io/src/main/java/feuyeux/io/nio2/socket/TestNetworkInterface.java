package feuyeux.io.nio2.socket;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 *
 * @author feuyeux@gmail.com 2012-06-06
 */
public final class TestNetworkInterface {

    public static void main(String[] argv) throws Exception {
        Enumeration<NetworkInterface> enumInterfaces = NetworkInterface.getNetworkInterfaces();
        while (enumInterfaces.hasMoreElements()) {
            NetworkInterface net = enumInterfaces.nextElement();

            if (net.getDisplayName().contains("Virtual")) continue;
            if (net.isVirtual()) continue;
            if (net.isLoopback()) continue;
            if (!net.isUp()) continue;

            System.out.println("----");
            System.out.println("Network Interface Display Name: " + net.getDisplayName());
            System.out.println(net.getDisplayName() + " is up and running ?" + net.isUp());
            System.out.println(net.getDisplayName() + " Supports Multicast: " + net.supportsMulticast());
            System.out.println(net.getDisplayName() + " Name: " + net.getName());
            System.out.println(net.getDisplayName() + " Is Virtual:  " + net.isVirtual());
            System.out.println("IP addresses:");
            Enumeration<InetAddress> enumIP = net.getInetAddresses();
            while (enumIP.hasMoreElements()) {
                InetAddress ip = enumIP.nextElement();
                System.out.println(ip);
            }
        }
    }
}
