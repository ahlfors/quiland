package feuyeux.io.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 *
 * Created by feuyeux@gmail.com
 * Date: Mar 04 2014
 * Time: 1:45 PM
 */
public class Tooling {
    public static InetAddress getAddress() {
        try {
            Enumeration<NetworkInterface> enumInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumInterfaces.hasMoreElements()) {
                NetworkInterface net = enumInterfaces.nextElement();
                if (net.getDisplayName().contains("Virtual")) continue;
                if (net.isVirtual()) continue;
                if (net.isLoopback()) continue;
                if (!net.isUp()) continue;
                Enumeration<InetAddress> enumIP = net.getInetAddresses();
                while (enumIP.hasMoreElements()) {
                    InetAddress ip = enumIP.nextElement();
                    return ip;
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public static void main(String[] args) throws SocketException {
        InetAddress address = getAddress();
        System.out.println(address.getHostName());
        System.out.println(address.getHostAddress());
    }
}
