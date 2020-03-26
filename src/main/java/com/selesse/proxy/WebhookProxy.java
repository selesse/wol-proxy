package com.selesse.proxy;

import com.selesse.wol.WolService;
import com.selesse.wol.packet.WolPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.net.NetworkInterface;
import java.util.Optional;

public class WebhookProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookProxy.class);

    public void startProxy(int port) {
        Spark.port(port);
        Spark.get("/wol", (req, res) -> {
            String macAddress = req.queryParamOrDefault("send", null);
            if (macAddress != null) {
                String interfaceName = req.queryParamOrDefault("interface", "eth0");
                LOGGER.info("Sending WOL packet to {} through interface {}", macAddress, interfaceName);
                WolPacket wolPacket = new WolPacket(macAddress);
                WolService wolService = new WolService(wolPacket);
                NetworkInterface networkInterface =
                        Optional.ofNullable(NetworkInterface.getByName(interfaceName))
                                .orElseThrow(() ->
                                        new RuntimeException("Could not find network interface " + interfaceName)
                                );
                wolService.sendWakeOnLan(networkInterface);
            }
            LOGGER.info("Received request: {}", req.userAgent());
            return "Welcome to the world of WOL";
        });

        Spark.post("/wol", (req, res) -> {
            LOGGER.info("{}", req.body());
            return "OK";
        });
    }
}
