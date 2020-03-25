package com.selesse.wol;

import com.selesse.wol.packet.WolPacket;
import org.junit.Test;

import java.net.NetworkInterface;
import java.net.SocketException;

public class WolServiceTest {

    @Test
    public void send() throws SocketException {
        new WolService(new WolPacket("aa:aa:aa:aa:aa:ab")).sendWakeOnLan(NetworkInterface.getByName("eth0"));
    }
}