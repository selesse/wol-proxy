package com.selesse.notification.listener;

import com.selesse.notification.client.Client;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ListenerTest {
    @Test
    public void test() throws Exception {
        Listener listener2 = new Listener();
        Runnable serverThread = () -> {
            Listener listener = new Listener();
            try {
                listener.listen();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread t = new Thread(serverThread);
        t.setDaemon(true);
        t.start();

        TimeUnit.MILLISECONDS.sleep(200);

        Client client = new Client("localhost", Listener.PORT);
        client.ping();
    }

}