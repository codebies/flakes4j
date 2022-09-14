package org.codebies.flakes4j.identifier;

import java.util.Random;

final class RandomUtils {

    static int getRandom16Bits(){
        return getRandomBits(4,4);
    }

    static int getRandomBits(int first, int... sizes){
        Random random = new Random();
        int value = random.nextInt(getBound(first));
        for(int size:sizes)
            value = (value << size) | random.nextInt(getBound(size));
        return value;
    }

    static int getBound(int bits){
        return ~(-1 << bits);
    }


}
