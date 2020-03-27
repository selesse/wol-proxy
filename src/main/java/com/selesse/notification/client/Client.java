package com.selesse.notification.client;

import com.selesse.notification.Message;
import com.selesse.wol.DesktopWakeOnLanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private final String host;
    private final int port;

    public Client (String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void ping() throws IOException, ClassNotFoundException {
        Socket socket = new Socket(host, port);
        InputStream inputStream = socket.getInputStream();

        while (true) {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Object o = objectInputStream.readObject();
            if (o instanceof Message) {
                String message = ((Message) o).getValue();
                LOGGER.info("Received message={} from server", message);

                if (message.equals("wol")) {
                    new DesktopWakeOnLanService().sendWakeOnLan();
                }
            }
        }
    }
}
