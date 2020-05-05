package com.selesse.notification.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class SocketConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketConnector.class);

    private final String host;
    private final int port;

    public SocketConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private Socket connectToServer() {
        try {
            return new Socket(host, port);
        } catch (IOException e) {
            LOGGER.error("Unable to connect to server, retrying...", e);
            try {
                TimeUnit.SECONDS.sleep(30);
                return connectToServer();
            } catch (InterruptedException interruptedException) {
                throw new RuntimeException(e);
            }
        }
    }

    public InputStream connectToInputStream() {
        try {
            return connectToServer().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
