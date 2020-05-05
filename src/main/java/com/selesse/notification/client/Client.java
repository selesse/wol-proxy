package com.selesse.notification.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private final SocketConnector socketConnector;
    private ScheduledExecutorService executorService;

    public Client(String host, int port) {
        this.socketConnector = new SocketConnector(host, port);
        this.executorService = Executors.newScheduledThreadPool(2);
    }

    public void ping() {
        PingThread pingThread = new PingThread(socketConnector, executorService);
        pingThread.start();

        ScheduledFuture<?> scheduledFuture =
                executorService.scheduleAtFixedRate(new HeartbeatThread(pingThread), 0, 5, TimeUnit.MINUTES);
        Runnable exceptionAwareScheduledFuture = () -> {
            try {
                scheduledFuture.get();
            } catch (InterruptedException e) {
                LOGGER.error("Scheduled execution was interrupted", e);
            } catch (CancellationException e) {
                LOGGER.warn("Heartbeat has been cancelled", e);
            } catch (ExecutionException e) {
                if (e.getCause() instanceof ClientRuntimeException) {
                    LOGGER.error("Client experienced a runtime exception, restarting ping thread", e.getCause().getCause());
                    restartPingThread();
                } else {
                    LOGGER.error("Uncaught exception in scheduled execution, canceling heartbeat", e);
                    scheduledFuture.cancel(true);
                }
            }
        };
        executorService.execute(exceptionAwareScheduledFuture);
    }

    private void restartPingThread() {
        executorService.shutdown();
        executorService = Executors.newScheduledThreadPool(2);
        ping();
    }
}

