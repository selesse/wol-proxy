package com.selesse.proxy;

import com.selesse.notification.listener.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class WebhookProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookProxy.class);

    public void startProxy(int port, Listener listener) {
        Spark.port(port);
        Spark.get("/wol", (req, res) -> {
            listener.onRequestReceived();
            return "Welcome to the world of WOL";
        });

        Spark.post("/wol", (req, res) -> {
            LOGGER.info("{}", req.body());
            listener.onRequestReceived();
            return "OK";
        });
    }
}
