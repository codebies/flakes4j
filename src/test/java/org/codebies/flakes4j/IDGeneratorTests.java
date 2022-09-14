package org.codebies.flakes4j;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IDGeneratorTests {

    @Test
    public void createIDGenerator() {
        Application idGenerator = new Application();
    }

    @Test
    public void nextId_returnGreaterThanZero() {
        Application idGenerator = new Application();
        Assertions.assertTrue(idGenerator.nextId() > 0);
    }

    @Test
    public void nextIds_returnUniqueIds() {
        Application idGenerator = new Application();
        long first = idGenerator.nextId();
        long second = idGenerator.nextId();
        Assertions.assertNotEquals(first, second);
    }


    @Test
    public void testInetAddress() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getByName("10.129.10.20");
        for (byte b : inetAddress.getAddress()) {
            System.out.println(b);
            System.out.println(Byte.toUnsignedInt(b));
        }


    }


}
