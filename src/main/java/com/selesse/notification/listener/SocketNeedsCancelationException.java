package com.selesse.notification.listener;

import java.net.SocketException;

public class SocketNeedsCancelationException extends RuntimeException {
    public SocketNeedsCancelationException(SocketException cause) {
        super(cause);
    }
}
