package com.selesse.wol;

import com.selesse.wol.packet.WolPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

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
            NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
            DatagramChannel datagramChannel = DatagramChannel.open();
            datagramChannel.bind(null);
            datagramChannel.socket().setBroadcast(true);
            datagramChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);
            datagramChannel.send(ByteBuffer.wrap(wolPacket.packetContents()), new InetSocketAddress(BROADCAST_ADDRESS, 9));
        } catch (IOException e) {
            LOGGER.info("Error while sending packet", e);
            throw new RuntimeException(e);
        }
    }
}
