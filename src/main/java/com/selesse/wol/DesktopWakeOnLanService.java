package com.selesse.wol;

import com.selesse.wol.packet.WolPacket;

import java.net.NetworkInterface;
import java.net.SocketException;

public class DesktopWakeOnLanService {
    public void sendWakeOnLan() throws SocketException {
        WolService wolService = new WolService(new WolPacket(System.getenv("WOL_MAC_ADDRESS")));
        wolService.sendWakeOnLan(NetworkInterface.getByName(System.getenv().getOrDefault("WOL_NETWORK_INTERFACE", "eth0")));
    }
}
