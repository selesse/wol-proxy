package com.selesse.notification.client;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class PingThread {
    private final SocketConnector socketConnector;
    private final ExecutorService executorService;
    private PingHandler pingHandler;
    private Future<?> pingHandlerThread;

    public PingThread(SocketConnector socketConnector, ExecutorService executorService) {
        this.socketConnector = socketConnector;
        this.executorService = executorService;
    }

    public void start() {
        PingHandler pingHandler = new PingHandler(socketConnector);
        this.pingHandler = pingHandler;
        pingHandlerThread = executorService.submit(pingHandler);
    }

    public LocalDateTime getLastHeartbeat() {
        return pingHandler.getLastHeartbeat();
    }

    public void interrupt() {
        pingHandlerThread.cancel(true);
    }
}
