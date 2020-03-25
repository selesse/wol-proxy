package com.selesse.wol;

import com.selesse.wol.packet.WolPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

public class WolService {
    private static Logger LOGGER = LoggerFactory.getLogger(WolService.class);
    private static InetAddress BROADCAST_ADDRESS;

    static {
        try {
            BROADCAST_ADDRESS = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException e) {
            throw new RuntimeException("Could not find broadcast address");
        }
    }

    private WolPacket wolPacket;

    public WolService(WolPacket wolPacket) {
        this.wolPacket = wolPacket;
    }

    public void send(String interfaceName) {
        try {
            int port = 9;
            DatagramPacket datagramPacket = new DatagramPacket(
                    wolPacket.packetContents(),
                    wolPacket.packetContents().length,
                    BROADCAST_ADDRESS,
                    port
            );
            NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
            InetAddress inetAddress = networkInterface.getInetAddresses().nextElement();

            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            LOGGER.info("Error while sending packet", e);
            throw new RuntimeException(e);
        }
    }
}
