package org.codebies.flakes4j.utils;

import java.util.Random;

public final class RandomUtils {

    public static int getRandomSegment(int first, int... sizes){
        Random random = new Random();
        int value = random.nextInt(getBound(first));
        for(int size:sizes)
            value = (value << size) | random.nextInt(getBound(size));
        return value;
    }

    private static int getBound(int bits){
        return ~(-1 << bits);
    }


}
