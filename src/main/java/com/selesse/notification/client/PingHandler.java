package com.selesse.notification.client;

import com.selesse.notification.Message;
import com.selesse.wol.DesktopWakeOnLanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;

public class PingHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PingHandler.class);
    private final SocketConnector socketConnector;
    private LocalDateTime lastHeartbeat;

    public PingHandler(SocketConnector socketConnector) {
        this.socketConnector = socketConnector;
        this.lastHeartbeat = LocalDateTime.now();
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socketConnector.connectToInputStream();
            while (true) {
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                Object o = objectInputStream.readObject();
                if (o instanceof Message) {
                    lastHeartbeat = LocalDateTime.now();
                    String message = ((Message) o).getValue();
                    LOGGER.info("Received message={} from server", message);

                    if (message.equals("wol")) {
                        new DesktopWakeOnLanService().sendWakeOnLan();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new ClientRuntimeException(e);
        }
    }

    public LocalDateTime getLastHeartbeat() {
        return lastHeartbeat;
    }
}
