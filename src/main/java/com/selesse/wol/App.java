package com.selesse.wol;

import com.google.common.base.Strings;
import com.selesse.wol.packet.WolPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.net.DatagramPacket;

public class App {
    private static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        Spark.port(8080);
        Spark.get("/wol", (req, res) -> {
            String macAddress = req.queryParamOrDefault("send", null);
            if (macAddress != null) {
                LOGGER.info("Sending WOL packet to {}", macAddress);
                WolPacket wolPacket = new WolPacket(macAddress);
                WolService wolService = new WolService(wolPacket);
                wolService.send("eth0");
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
