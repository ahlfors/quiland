package feuyeux.io.mina.tcp;

import feuyeux.io.mina.MinaTimeServer;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;

public class MinaTCPTimeServer extends MinaTimeServer {
    public MinaTCPTimeServer() {
        super(new NioSocketAcceptor());
    }

    public static void main(String[] args) throws IOException {
        new MinaTCPTimeServer().init();
    }
}