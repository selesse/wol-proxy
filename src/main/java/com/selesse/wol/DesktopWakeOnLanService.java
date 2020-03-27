package com.selesse.wol;

import com.selesse.wol.packet.WolPacket;

import java.net.NetworkInterface;
import java.net.SocketException;

public class DesktopWakeOnLanService {
    public void sendWakeOnLan() throws SocketException {
        WolService wolService = new WolService(new WolPacket("aa:aa:aa:aa:aa:ab"));
        wolService.sendWakeOnLan(NetworkInterface.getByName("eth0"));
    }
}
