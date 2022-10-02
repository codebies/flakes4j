package org.codebies.flakes4j;

import io.vavr.Function0;
import org.codebies.flakes4j.segment.SerialSegment;
import org.codebies.flakes4j.segment.SimpleSegment;
import org.codebies.flakes4j.utils.RandomUtils;

import java.util.function.Supplier;

public interface Segments {


    static Segment newConstantSegment(int size, int value){
        return  SimpleSegment.of(size, Function0.constant(value).memoized());
    }

    static Segment newConstantSegment(int size, Supplier<Integer> value){
        return SimpleSegment.of(size, Function0.of(value::get).memoized());
    }

    static Segment newRandomOnceSegment(int size){
        return SimpleSegment.of(size, Function0.of(()->RandomUtils.getRandomSegment(size)).memoized());
    }

    static Segment newRandomSegment(int size, Supplier<Integer> value){
        return SimpleSegment.of(size, Function0.of(value::get));
    }

    static Segment newRandomSegment(int size){
        return SimpleSegment.of(size, ()->RandomUtils.getRandomSegment(size));
    }

    static Segment newSerialSegment(int size){
        return SerialSegment.of(size);
    }

}
