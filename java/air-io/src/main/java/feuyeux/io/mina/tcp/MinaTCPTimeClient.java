package feuyeux.io.mina.tcp;

import feuyeux.io.mina.MinaTimeClient;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaTCPTimeClient extends MinaTimeClient {

    public MinaTCPTimeClient() {
        super(new NioSocketConnector());
    }

    public static void main(String[] args) throws Throwable {
        new MinaTCPTimeClient().connect();
    }
}
