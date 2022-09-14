package org.codebies.flakes4j;

import org.codebies.flakes4j.sequence.Sequence;
import org.codebies.flakes4j.sequence.Sequences;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Application {


    public static void main(String[] args) throws InterruptedException {

       // testByAccumulated();

        Sequence<Long> idGenerator = Sequences.newSecondsBasedSequence();
        for(int i=0;i<10;i++){
            print(idGenerator.next());
        }

    }

    private static void testByAccumulated() throws InterruptedException {
        long t0 = System.currentTimeMillis();

        Sequence<Long> idGenerator = Sequences.newSecondsBasedSequence();
        List<Long> ids = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            executeThreadedCycle(idGenerator, ids, 65000);

           // ids = Collections.synchronizedList(new ArrayList<>());
            long end = System.currentTimeMillis();

          //  long period = 1000 - (end-start) + 100;
           // System.out.println("Period - "+ period+ "/" + start + " / "+ end);
           // if(period > 0)
                Thread.sleep(1000);
            //idGenerator.reset();
        }

        long t1 = System.currentTimeMillis();
        System.out.println("Time taken - "+ (t1-t0));
        printStats(ids);
    }

    private static void testBySeperate() throws InterruptedException {
        long t0 = System.currentTimeMillis();

        Sequence<Long> idGenerator = Sequences.newSecondsBasedSequence();
        List<Long> ids = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis()/10;
            executeThreadedCycle(idGenerator, ids, 65000);
            printStats(ids);
            ids = Collections.synchronizedList(new ArrayList<>());
            long end = System.currentTimeMillis()/10;

           /* long period = 10 - (end-start) + 1;
            if(period > 0)
                Thread.sleep(period);*/
           // idGenerator.reset();
        }

        long t1 = System.currentTimeMillis();
        System.out.println("Time taken - "+ (t1-t0));
    }

    private static void printStats(List<Long> ids) {
        Map<Long, Long> count = ids.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Set<Long> set = new HashSet<>(ids);
        long dup = count.entrySet().stream().filter(kv -> kv.getValue() > 1).count();

        // System.out.printf("\nCount is %d\n", ids.size());
        //System.out.printf("Count is %d  | unique is %d | duplicates is %d \n",ids.size(), set.size(), dup);
        if (dup > 0) {
            System.out.println("DUPLICATION :: ::");
            System.out.printf("Count is %d  | unique is %d | duplicates is %d \n", ids.size(), set.size(), dup);
            count.entrySet().stream().filter(kv -> kv.getValue() > 1).forEach(entry -> System.out.println(format(entry)));
        }
       /* for(Long i:ids){
            //System.out.println(i);
            print(i);
        }*/
    }

    private static void executeThreadedCycle(Sequence<Long> idGenerator, List<Long> ids, int count) throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 1; i <= count/5; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 5; j++) {
                    long id = idGenerator.next();
                    ids.add(id);
                    counter.incrementAndGet();
                }
            }).start();
        }

        while (counter.get() < count) {
            Thread.sleep(3);
        }
    }

    private static void executeCycle(Sequence<Long> idGenerator, List<Long> ids, int count) throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 1; i <= count; i++) {

            long id = idGenerator.next();
            ids.add(id);
            counter.incrementAndGet();
        }
    }

    private static void print(long number) {
        String binary = Long.toBinaryString(number);
        System.out.printf("%d (%d)=> %s (%d)\n", number, (number + "").length(), binary, binary.length());
    }

    private static String format(Map.Entry<Long,Long> entry) {
        return format(entry.getKey(), entry.getValue());
    }

    private static String format(long value, long count) {
        String binary = Long.toBinaryString(value);
        long timeMask = 0xffffffffff000000L;
        long serialMask = 0xff0000L;
        return String.format("%d | %s (%d) | %d | %d | %d ",value,  binary, binary.length(), (value & timeMask) >> 24 , (value & serialMask) >> 16, count);
    }

    public long nextId() {
        return System.currentTimeMillis();
    }




}
