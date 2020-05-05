package com.selesse.notification.listener;

import org.junit.Test;

import java.util.concurrent.Executors;

public class ListenerTest {
    @Test
    public void test() throws Exception {
        // Needed for the SLF4J LOGGER to not crash
        //noinspection unused
        Listener unused = new Listener();

        int listenerPort = 8888;
        Executors.newSingleThreadExecutor().execute(() -> {
                    Listener listener = new Listener();
                    listener.listen(listenerPort);
                }
        );

        Client client = new Client("localhost", listenerPort);
        client.ping();
    }

}