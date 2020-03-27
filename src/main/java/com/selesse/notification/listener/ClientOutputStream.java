package com.selesse.notification.listener;

import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;

public class ClientOutputStream {
    private final OutputStream outputStream;
    private ReentrantLock reentrantLock;

    public ClientOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.reentrantLock = new ReentrantLock();
    }

    public synchronized OutputStream get() {
        reentrantLock.lock();
        return outputStream;
    }

    public void release() {
        reentrantLock.unlock();
    }
}
