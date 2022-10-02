package org.codebies.flakes4j.supplier;

import org.codebies.flakes4j.Segment;
import org.codebies.flakes4j.Segments;

public final class Serial implements ValueSegmentSupplier {

    private final int size;

    private Serial(int size) {
        this.size = size;
    }

    public static ValueSegmentSupplier of(int size){
        return new Serial(size);
    }

    @Override
    public Segment newInstance() {
        return Segments.newSerialSegment(size);
    }
}
