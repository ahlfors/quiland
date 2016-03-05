package feuyeux.io.notireq;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.net.Socket;

public class SendingNotification extends Thread {
    private static final Logger logger = LogManager.getLogger(SendingNotification.class);
    private final String id;
    private final Socket socket;

    public SendingNotification(String confId_sdId, Socket socket) {
        id = confId_sdId;
        this.socket = socket;
    }

    @Override
    public void run() {
        Helper.getInstance().add(id);
        byte[] buffer;
        try (OutputStream outputStream = socket.getOutputStream()) {
            buffer = (id + '\n').getBytes();
            outputStream.write(buffer);
            outputStream.flush();
        } catch (Exception ignored) {
            logger.error("Send failed.");
            try {
                socket.close();
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }
}
