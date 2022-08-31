package org.rxshine.flakes4j;

import org.junit.jupiter.api.Test;

public class IdentifiersTests {

    @Test
    public void verifyId() {
        int id = Identifier.getMachineId();
        System.out.println(id);
        System.out.println(Integer.toBinaryString(id));
    }
}
