package com.selesse.notification.listener;

import com.selesse.notification.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.*;

public class ClientHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket client;
    private final ScheduledExecutorService executorService;
    private ClientOutputStream clientOutputStream;
    private SocketAddress remoteSocketAddress;
    private boolean isAlive;

    public ClientHandler(Socket client) {
        this.client = client;
        this.remoteSocketAddress = client.getRemoteSocketAddress();
        this.executorService = Executors.newScheduledThreadPool(2);
        this.isAlive = true;
    }

    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Handling incoming client {}", remoteSocketAddress);
            this.clientOutputStream = new ClientOutputStream(client.getOutputStream());

            PingOutput pingOutput = new PingOutput(client, clientOutputStream);
            ScheduledFuture<?> scheduledFuture =
                    executorService.scheduleAtFixedRate(pingOutput, 0, 3, TimeUnit.MINUTES);
            Runnable r = () -> {
                try {
                    scheduledFuture.get();
                } catch (InterruptedException e) {
                    LOGGER.error("Scheduled execution was interrupted", e);
                } catch (CancellationException e) {
                    LOGGER.warn("Watcher thread has been cancelled", e);
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof SocketNeedsCancelationException) {
                        LOGGER.info("Socket closed, shutting down for client {}", remoteSocketAddress);
                    } else {
                        LOGGER.error("Uncaught exception in scheduled execution, aborting thread for client {}",
                                remoteSocketAddress, e);
                    }
                    scheduledFuture.cancel(true);
                    this.isAlive = false;
                }
            };
            executorService.execute(r);
        } catch (IOException e) {
            LOGGER.error("Error obtaining OutputStream", e);
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
