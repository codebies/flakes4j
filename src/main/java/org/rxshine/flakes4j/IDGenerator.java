package org.rxshine.flakes4j;

import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {

    private final AtomicInteger sequence;

    public IDGenerator(){
        sequence = new AtomicInteger(1);
    }

    public long nextId() {

        return System.currentTimeMillis();
    }

}
