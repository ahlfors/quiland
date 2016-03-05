package feuyeux.io.mina.udp;

import feuyeux.io.mina.MinaTimeClient;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

public class MinaUDPTimeClient extends MinaTimeClient {
    public MinaUDPTimeClient() {
        super(new NioDatagramConnector());
    }

    public static void main(String[] args) throws Throwable {
        new MinaUDPTimeClient().connect();
    }
}
