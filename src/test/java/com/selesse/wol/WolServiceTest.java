package com.selesse.wol;

import com.selesse.wol.packet.WolPacket;
import org.junit.Test;

import static org.junit.Assert.*;

public class WolServiceTest {

    @Test
    public void send() {
        new WolService(new WolPacket("aa:aa:aa:aa:aa:ab")).send("en0");
    }
}