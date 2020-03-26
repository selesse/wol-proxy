package com.selesse.wol;

import com.selesse.notification.client.Client;
import com.selesse.notification.listener.Listener;
import com.selesse.proxy.WebhookProxy;
import com.selesse.wol.packet.WolPacket;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Optional;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
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
                startServer(serverPort);
                break;
            case "--proxy":
                int proxyPort = Optional.ofNullable(args[1]).map(Integer::valueOf).orElse(8080);
                startProxy(proxyPort);
                break;
        }
    }

    private static void sendWol(String macAddress, String networkInterfaceName) throws SocketException {
        WolPacket wolPacket = new WolPacket(macAddress);
        WolService wolService = new WolService(wolPacket);
        wolService.sendWakeOnLan(NetworkInterface.getByName(networkInterfaceName));
    }

    private static void pingClient(String host, int port) throws IOException, ClassNotFoundException {
        Client client = new Client(host, port);
        client.ping();
    }

    private static void startServer(int port) {
        Thread t = new Thread(() -> {
            Listener listener = new Listener();
            try {
                listener.listen(port);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    private static void startProxy(int port) {
        WebhookProxy webhookProxy = new WebhookProxy();
        webhookProxy.startProxy(port);
    }
}
