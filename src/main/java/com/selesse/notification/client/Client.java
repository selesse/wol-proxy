package com.selesse.notification.client;

import com.selesse.notification.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private final String host;
    private final int port;

    public Client (String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void ping() throws IOException, ClassNotFoundException {
        Socket socket = new Socket(host, port);
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Object o = objectInputStream.readObject();
        if (o instanceof Message) {
            String message = ((Message) o).getValue();
            logger.info("Received {} from server", message);
        }
        // while (true) receive message
        // server sends pings every 30 minutes
        // client resilient to this
        // add logging
        // thread per client

        Object newObject = objectInputStream.readObject();
        logger.info("Received second object: {}", newObject);
    }
}
