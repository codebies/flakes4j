package org.codebies.flakes4j.sequence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SequencesTest {

    @Test
    public void testIDGeneration() throws InterruptedException {
        List<Long> ids = testByAccumulated(Sequences.newSecondsBasedSequence(), 100, 65000, 1000);
        Map<Long, Long> countMap = ids.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Assertions.assertEquals(ids.size(), countMap.size());
    }

    private List<Long> testByAccumulated(Sequence<Long> sequence, int iteration, int sequenceByIteration, int milliSeconds) throws InterruptedException {
        List<Long> ids = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < iteration; i++) {
            executeThreadedCycle(sequence, ids, sequenceByIteration);
            Thread.sleep(milliSeconds);
        }
        return ids;
    }

    private void executeThreadedCycle(Sequence<Long> sequence, List<Long> ids, int count) throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 1; i <= count/5; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 5; j++) {
                    long id = sequence.next();
                    ids.add(id);
                    counter.incrementAndGet();
                }
            }).start();
        }
        if(count%5 > 0){
            new Thread(() -> {
                for (int j = 1; j <= count%5; j++) {
                    long id = sequence.next();
                    ids.add(id);
                    counter.incrementAndGet();
                }
            }).start();
        }
        while (counter.get() < count) {
            Thread.sleep(3);
        }
    }



}
