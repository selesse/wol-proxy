package com.selesse.notification.listener;

import com.selesse.notification.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PingOutput implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PingOutput.class);
    private final Socket client;
    private ClientOutputStream outputStream;

    public PingOutput(Socket client, ClientOutputStream outputStream) {
        this.client = client;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Pinging {}", client.getRemoteSocketAddress());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream.get());
            objectOutputStream.writeObject(new Message("ping"));
            objectOutputStream.flush();
            outputStream.release();
        } catch (IOException e) {
            LOGGER.info("Interrupted while trying to ping", e);
            outputStream.release();
        }
    }
}
