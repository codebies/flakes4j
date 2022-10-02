package org.codebies.flakes4j.supplier;

import org.codebies.flakes4j.Segment;
import org.codebies.flakes4j.Segments;

import java.util.function.Supplier;

public final class Constant implements ValueSegmentSupplier {

    private final int size;
    private final Supplier<Integer> value;

    private Constant(int size, int value) {
        this(size, ()->value);
    }

    private Constant(int size, Supplier<Integer> value) {
        this.size = size;
        this.value = value;
    }

    public static ValueSegmentSupplier of(int size, int value){
        return new Constant(size, value);
    }

    public static ValueSegmentSupplier of(int size, Supplier<Integer> value){
        return new Constant(size, value);
    }

    @Override
    public Segment newInstance(){
        return Segments.newConstantSegment(size,value);
    }

}
