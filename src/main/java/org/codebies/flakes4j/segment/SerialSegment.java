package org.codebies.flakes4j.segment;

import org.codebies.flakes4j.Segment;
import org.codebies.flakes4j.TickChangeEventListener;
import org.codebies.flakes4j.exception.InvalidInputRangeException;
import org.codebies.flakes4j.exception.SequenceLimitExceeded;

public class SerialSegment implements Segment, TickChangeEventListener {
    private final int size;
    private final long mask;
    private long value = -1;

    private SerialSegment(int size) {
        validateInput(size);
        this.size = size;
        this.mask = ~(-1L << size);
    }

    private void validateInput(int size) {
        if(size > 63 || size <1)
            throw new InvalidInputRangeException(0,63);
    }

    public static Segment of(int size){
        return new SerialSegment(size);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public long next() {
        this.value++;
        if(this.value>0 && (this.value & mask) ==0)
            throw new SequenceLimitExceeded(String.format("Constraint Value : %d (%d bits), Exceeded Value: %d ", mask, size,value));
        return value;
    }

    @Override
    public void onTickChange() {
        this.value = -1;
    }
}
