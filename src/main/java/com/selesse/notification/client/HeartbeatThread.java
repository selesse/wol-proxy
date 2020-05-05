package com.selesse.notification.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class HeartbeatThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatThread.class);
    private final PingThread pingThread;

    public HeartbeatThread(PingThread pingThread) {
        this.pingThread = pingThread;
    }

    @Override
    public void run() {
        LocalDateTime lastHeartbeat = pingThread.getLastHeartbeat();

        if (lastHeartbeat.isBefore(LocalDateTime.now().minus(5, ChronoUnit.MINUTES))) {
            LOGGER.info("Last heartbeat was at {} - reconnecting", lastHeartbeat);
            pingThread.interrupt();
            pingThread.start();
        }
    }
}
