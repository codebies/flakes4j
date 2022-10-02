package org.codebies.flakes4j.supplier;

import org.codebies.flakes4j.Segment;
import org.codebies.flakes4j.Segments;

public class RandomOnce implements ValueSegmentSupplier {

    private final int size;

    private RandomOnce(int size) {
        this.size = size;
    }


    public static ValueSegmentSupplier of(int size){
        return new RandomOnce(size);
    }

    @Override
    public Segment newInstance() {
        return Segments.newRandomOnceSegment(size);
    }
}
