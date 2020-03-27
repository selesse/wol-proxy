package com.selesse.notification.listener;

import com.selesse.notification.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket client;
    private final ScheduledExecutorService executorService;
    private ClientOutputStream clientOutputStream;

    public ClientHandler(Socket client) {
        this.client = client;
        this.executorService = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Handling incoming client {}", client.getRemoteSocketAddress());
            this.clientOutputStream = new ClientOutputStream(client.getOutputStream());

            PingOutput pingOutput = new PingOutput(client, clientOutputStream);
            executorService.scheduleAtFixedRate(pingOutput, 0, 3, TimeUnit.MINUTES);
        } catch (IOException e) {
            LOGGER.info("Error trying to send ping", e);
        }
    }

    public void sendWolMessage() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientOutputStream.get());
            objectOutputStream.writeObject(new Message("wol"));
            objectOutputStream.flush();
            clientOutputStream.release();
        } catch (IOException e) {
            LOGGER.info("Error trying to send WOL message", e);
            clientOutputStream.release();
        }
    }

}
