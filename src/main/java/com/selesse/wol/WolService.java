package com.selesse.wol;

import com.selesse.wol.packet.WolPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Objects;

public class WolService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WolService.class);
    private static final int WOL_PORT = 9;
    private static final InetAddress DEFAULT_BROADCAST_ADDRESS;

    static {
        try {
            DEFAULT_BROADCAST_ADDRESS = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException e) {
            throw new RuntimeException("Could not find broadcast address");
        }
    }

    private final WolPacket wolPacket;

    public WolService(WolPacket wolPacket) {
        this.wolPacket = wolPacket;
    }

    public void sendWakeOnLan(NetworkInterface networkInterface) {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(
                    wolPacket.getPacketContents(),
                    wolPacket.getPacketContents().length
            );
            MulticastSocket multicastSocket = new MulticastSocket();
            multicastSocket.setBroadcast(true);
            InetAddress broadcastAddress = getDefaultBroadcastAddress(networkInterface);
            LOGGER.info("Sending WoL packet to interface={} with broadcast address={} and MAC address={}",
                    networkInterface.getDisplayName(),
                    broadcastAddress.getHostAddress(),
                    wolPacket.getMacAddress());
            datagramPacket.setAddress(broadcastAddress);
            datagramPacket.setPort(WOL_PORT);
            multicastSocket.send(datagramPacket);
        } catch (IOException e) {
            LOGGER.info("Error while sending packet", e);
            throw new RuntimeException(e);
        }
    }

    private static InetAddress getDefaultBroadcastAddress(NetworkInterface networkInterface) {
        return networkInterface.getInterfaceAddresses().stream()
                        .map(InterfaceAddress::getBroadcast)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(DEFAULT_BROADCAST_ADDRESS);
    }
}
