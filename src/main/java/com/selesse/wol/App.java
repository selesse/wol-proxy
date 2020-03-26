package com.selesse.wol;

import com.selesse.notification.client.Client;
import com.selesse.notification.listener.Listener;
import com.selesse.wol.packet.WolPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Optional;

public class App {
    private static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 0) {
            switch (args[0]) {
                case "--wol":
                    WolPacket wolPacket = new WolPacket(args[1]);
                    WolService wolService = new WolService(wolPacket);
                    wolService.sendWakeOnLan(NetworkInterface.getByName(args[2]));
                    break;
                case "--client":
                    Client client = new Client(args[1], Integer.parseInt(args[2]));
                    client.ping();
                    break;
                case "--server":
                    Thread t = new Thread(() -> {
                        Listener listener = new Listener();
                        try {
                            listener.listen();
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    t.start();
                    break;
            }
        }
        else {
            Spark.port(8080);
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
}
