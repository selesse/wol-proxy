package com.selesse.notification.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private final int THREAD_POOL_SIZE = 2;
    private final SocketConnector socketConnector;
    private ScheduledExecutorService executorService;

    public Client(String host, int port) {
        this.socketConnector = new SocketConnector(host, port);
        this.executorService = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
    }

    public void ping() {
        PingThread pingThread = new PingThread(socketConnector, executorService);
        pingThread.start();

        Runnable exceptionTolerantHeartbeat = () -> {
            try {
                new HeartbeatThread(pingThread).run();
            } catch (Throwable e) {
                LOGGER.error("Uncaught exception in scheduled execution, restarting heartbeat", e);
                restartPingThread();
            }
        };
        executorService.scheduleAtFixedRate(exceptionTolerantHeartbeat, 1, 5, TimeUnit.MINUTES);
    }

    private void restartPingThread() {
        executorService.shutdown();
        executorService = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
        ping();
    }
}

