package com.selesse.notification.listener;

import com.selesse.notification.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class PingOutput implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PingOutput.class);
    private ClientOutputStream outputStream;

    public PingOutput(ClientOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
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
