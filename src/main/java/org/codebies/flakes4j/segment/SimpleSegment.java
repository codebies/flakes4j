package org.codebies.flakes4j.segment;

import org.codebies.flakes4j.Segment;
import org.codebies.flakes4j.exception.InvalidInputException;
import org.codebies.flakes4j.utils.NumberUtils;

import java.util.function.Supplier;

import static org.codebies.flakes4j.utils.NumberUtils.*;

public final class SimpleSegment implements Segment {
    private final int size;
    private final Supplier<Integer> valueSupplier;

    private SimpleSegment(int size, Supplier<Integer> valueSupplier){
        this.size = size;
        this.valueSupplier = valueSupplier;
    }

    public static Segment of(int size, int value){
        return of(size,()->value);
    }

    public static Segment of(int size, Supplier<Integer> supplier){
        if(size <= 0 || size > 31)
            throw new InvalidInputException("size should be between 1 - 31");
        return new SimpleSegment(size,supplier);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public long next() {
        long value = valueSupplier.get();
        if(!isInBitRange(size,value)){
            throw new InvalidInputException();
        }
        return value;
    }
}
