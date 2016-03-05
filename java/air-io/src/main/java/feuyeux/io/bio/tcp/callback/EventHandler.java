package feuyeux.io.bio.tcp.callback;

import feuyeux.io.common.ENV;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class EventHandler {
    private static final Logger logger = LogManager.getLogger(EventHandler.class.getName());
    private final String connectName;

    public EventHandler(String connectName) {
        this.connectName = connectName;
    }

    public void onMessageReceived(final TCPConnection conn, final String receivedMsg) {
        System.out.println("<--:" + receivedMsg);

        if (ENV.QUIT.equals(receivedMsg)) {
            try {
                conn.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }

        if (receivedMsg != null) {
            try {
                conn.sendMessage(receivedMsg);
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    public void onConnected(final TCPConnection conn) {
        System.out.println("New Connection created:" + conn);
        conn.registerMsg(this);
        new Thread(connectName + "-Handle-msg") {
            @Override
            public void run() {
                conn.receiveMessage();
            }
        }.start();
        new SystemInReadThread(conn).start();
    }
}