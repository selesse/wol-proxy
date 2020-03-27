package com.selesse.notification.listener;

import com.selesse.notification.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Found client: " + client.getRemoteSocketAddress());
            OutputStream outputStream = client.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(new Message("hello"));
            objectOutputStream.flush();

            TimeUnit.SECONDS.sleep(30);

            objectOutputStream.writeObject(new Message("hello"));
            objectOutputStream.flush();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
