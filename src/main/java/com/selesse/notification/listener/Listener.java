package com.selesse.notification.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Listener {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private List<ClientHandler> clientHandlers;

    public Listener() {
        this.clientHandlers = new ArrayList<>();
    }

    public void listen(int port) {
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket client = serverSocket.accept();
                client.setKeepAlive(true);
                client.setSoTimeout((int) TimeUnit.SECONDS.toMillis(2));
                LOGGER.info("Starting new thread");
                ClientHandler clientHandler = new ClientHandler(client);
                clientHandlers.add(clientHandler);
                executorService.submit(clientHandler);
            }
        } catch (IOException e) {
            LOGGER.error("Error trying to thread things", e);
        }
    }

    public void onRequestReceived() {
        clientHandlers.stream()
                .filter(ClientHandler::isAlive)
                .forEach(ClientHandler::sendWolMessage);
        clientHandlers = clientHandlers.stream().filter(ClientHandler::isAlive).collect(Collectors.toList());
    }
}
