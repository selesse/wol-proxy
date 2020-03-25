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
        if (args.length != 0) {
            WolPacket wolPacket = new WolPacket(args[0]);
            WolService wolService = new WolService(wolPacket);
            wolService.send(args[1]);
        }
        else {
            Spark.port(8080);
            Spark.get("/wol", (req, res) -> {
                String macAddress = req.queryParamOrDefault("send", null);
                if (macAddress != null) {
                    String networkInterface = req.queryParamOrDefault("interface", "eth0");
                    LOGGER.info("Sending WOL packet to {} through interface {}", macAddress, networkInterface);
                    WolPacket wolPacket = new WolPacket(macAddress);
                    WolService wolService = new WolService(wolPacket);
                    wolService.send(networkInterface);
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
}
