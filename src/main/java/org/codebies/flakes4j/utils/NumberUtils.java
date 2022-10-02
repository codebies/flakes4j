package org.codebies.flakes4j.utils;

import org.codebies.flakes4j.exception.InvalidInputRangeException;

public final class NumberUtils {

    public static boolean isInBitRange(int size, long value){
        return (value >> size) == 0;
    }

    public static long extract(long input, int from, int to){
        if(to > 64 || from > to)
            throw new InvalidInputRangeException();
        return (input >> (64 - to)) & ~(-1L << (to-from +1));
    }


}
