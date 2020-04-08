package com.selesse.proxy;

import com.selesse.notification.listener.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class WebhookProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookProxy.class);
    private static final String DEFAULT_MESSAGE = "Welcome to the world of WoL";

    public void startProxy(int port, Listener listener) {
        Spark.port(port);
        Spark.ipAddress("127.0.0.1");
        Spark.get("/wol", (req, res) -> DEFAULT_MESSAGE);

        Spark.post("/wol", (req, res) -> {
            LOGGER.info("Received post request for WoL");
            LOGGER.info("Body: {}, IP: {}", req.body(), req.ip());
            if (req.body().contains("'turn_on':'true'")) {
                listener.onRequestReceived();
            }
            return DEFAULT_MESSAGE;
        });
    }
}
