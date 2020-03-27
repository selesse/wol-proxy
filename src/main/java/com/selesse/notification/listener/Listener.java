package com.selesse.notification.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Listener {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    public void listen(int port) {
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket client = serverSocket.accept();
                LOGGER.info("Starting new thread");
                executorService.execute(new ClientHandler(client));
            }
        } catch (IOException e) {
            LOGGER.error("Error trying to thread things", e);
        }
    }
}
