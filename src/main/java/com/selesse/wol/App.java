package com.selesse.wol;

import com.selesse.notification.client.Client;
import com.selesse.notification.listener.Listener;
import com.selesse.proxy.WebhookProxy;
import com.selesse.wol.packet.WolPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Optional;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException {
        switch (args[0]) {
            case "--wol":
                String macAddress = args[1];
                String networkInterfaceName = args[2];
                sendWol(macAddress, networkInterfaceName);
                break;
            case "--client":
                String host = args[1];
                int port = Integer.parseInt(args[2]);
                pingClient(host, port);
                break;
            case "--server":
                int serverPort = Optional.ofNullable(args[1]).map(Integer::valueOf).orElse(8888);
                Listener listener = startServer(serverPort);
                int proxyPort = Optional.ofNullable(args[2]).map(Integer::valueOf).orElse(8080);
                startProxy(proxyPort, listener);
                break;
        }
    }

    private static void sendWol(String macAddress, String networkInterfaceName) throws SocketException {
        WolPacket wolPacket = new WolPacket(macAddress);
        WolService wolService = new WolService(wolPacket);
        wolService.sendWakeOnLan(NetworkInterface.getByName(networkInterfaceName));
    }

    private static void pingClient(String host, int port) {
        Client client = new Client(host, port);
        client.ping();
    }

    private static Listener startServer(int port) {
        Listener listener = new Listener();
        Thread t = new Thread(() -> {
            listener.listen(port);
        });
        t.start();
        LOGGER.info("Started listener service on port {}", port);
        return listener;
    }

    private static void startProxy(int port, Listener listener) {
        WebhookProxy webhookProxy = new WebhookProxy();
        webhookProxy.startProxy(port, listener);
    }
}
