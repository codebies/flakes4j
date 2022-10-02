package org.codebies.flakes4j.supplier;

import io.vavr.Function0;
import org.codebies.flakes4j.Segment;
import org.codebies.flakes4j.Segments;
import org.codebies.flakes4j.utils.RandomUtils;

import java.util.function.Supplier;

public class Random implements ValueSegmentSupplier {
    private final int size;
    private final Supplier<Integer> supplier;

    private Random(int size, Supplier<Integer> integerSupplier) {
        this.size = size;
        this.supplier = integerSupplier;
    }

    public static ValueSegmentSupplier of(int size){
        return new Random(size, Function0.of(()->RandomUtils.getRandomSegment(size)));
    }

    public static ValueSegmentSupplier of(int size, Supplier<Integer> supplier){
        return new Random(size, supplier);
    }


    @Override
    public Segment newInstance() {
        return Segments.newRandomSegment(size, supplier);
    }
}
