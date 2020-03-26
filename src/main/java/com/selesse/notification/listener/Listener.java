package com.selesse.notification.listener;

import com.selesse.notification.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Listener {
    private static final Logger logger = LoggerFactory.getLogger(Listener.class);
    public void listen(int port) throws IOException, InterruptedException {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket client = serverSocket.accept();
            logger.info("Found client: " + client.getRemoteSocketAddress());
            OutputStream outputStream = client.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(new Message("hello"));
            objectOutputStream.flush();

            TimeUnit.MINUTES.sleep(5);

            objectOutputStream.writeObject(new Message("hello"));
            objectOutputStream.flush();
        } catch (IOException | InterruptedException e) {
            throw e;
        }
    }
}
