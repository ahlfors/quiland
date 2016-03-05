package feuyeux.io.mina.udp;

import feuyeux.io.mina.MinaTimeServer;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import java.io.IOException;

public class MinaUDPTimeServer extends MinaTimeServer {
    public MinaUDPTimeServer() {
        super(new NioDatagramAcceptor());
        ((NioDatagramAcceptor) acceptor).getSessionConfig().setReuseAddress(true);
    }

    public static void main(String[] args) throws IOException {
        new MinaUDPTimeServer().init();
    }
}