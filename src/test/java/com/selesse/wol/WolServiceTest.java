package com.selesse.wol;

import com.selesse.wol.packet.WolPacket;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Optional;

public class WolServiceTest {

    @Test
    public void sendingAPacketWorks() throws SocketException {
        WolPacket wolPacket = new WolPacket("aa:aa:aa:aa:aa:ab");
        WolService wolService = new WolService(wolPacket);
        NetworkInterface networkInterface =
                Optional.ofNullable(NetworkInterface.getByName("eth0"))
                        .orElse(defaultNetworkInterface());
        wolService.sendWakeOnLan(networkInterface);
    }

    private NetworkInterface defaultNetworkInterface() throws SocketException {
        return Collections.list(NetworkInterface.getNetworkInterfaces()).
                stream().
                filter(x -> {
                    try {
                        return Collections.list(x.getInetAddresses()).contains(InetAddress.getLocalHost());
                    } catch (UnknownHostException e) {
                        return false;
                    }
                }).findFirst().orElseThrow(() -> new RuntimeException("Could not find any network interfaces"));
    }
}