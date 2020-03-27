package com.selesse.notification.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket client;
    private final ScheduledExecutorService executorService;

    public ClientHandler(Socket client) {
        this.client = client;
        this.executorService = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Handling incoming client {}", client.getRemoteSocketAddress());

            PingOutput pingOutput = new PingOutput(client.getOutputStream());
            executorService.scheduleAtFixedRate(pingOutput, 0, 20, TimeUnit.SECONDS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
