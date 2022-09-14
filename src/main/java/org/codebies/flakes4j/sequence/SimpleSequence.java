package org.codebies.flakes4j.sequence;

import org.codebies.flakes4j.exception.SequenceLimitExceeded;
import org.codebies.flakes4j.identifier.Identifiers;

import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

class SimpleSequence implements Sequence<Long> {

    private final static Logger logger = Logger.getLogger(SimpleSequence.class.getName());
    private final SliceSetting sliceSetting;
    private int sequence = 0;
    private final ReentrantLock mutex = new ReentrantLock();
    private final int serialMask;
    private final int id;
    private long elapsedTime;

    public SimpleSequence(SliceSetting sliceSetting) {
        this.sliceSetting = sliceSetting;
        this.id = Identifiers.IP_IDENTIFIER.getId();
        serialMask = getSerialMask();
    }

    public SimpleSequence(SliceSetting sliceSetting, int instanceId) {
        this.sliceSetting = sliceSetting;
        this.id = instanceId;
        serialMask = getSerialMask();
    }

    private int getSerialMask() {
        return ~(-1 << sliceSetting.getSerialLength());
    }

    public synchronized Long next() {
        mutex.lock();
        try {
            updateSequence();
            return generateSerial();
        }finally {
            mutex.unlock();
        }
    }

    private long generateSerial() {
        int serial = getNextSerial();
        return (elapsedTime << sliceSetting.getSerialLength() | serial) << sliceSetting.getIdLength() | id;
    }

    private void updateSequence() {
        long current = getTime();
        if (current != elapsedTime) {
            elapsedTime = current;
            sequence = 0;
        }
    }

    private int getNextSerial() {
        int serial = ++sequence;
        if ((serial & serialMask) == 0) {
            logger.log(Level.WARNING, "Limit Exceeded = {0]", serial);
            throw new SequenceLimitExceeded();
        }
        return serial;
    }

    private long getTime() {
        return sliceSetting.getTimeFunction().apply(DateUtils.getMillis());
    }


}
