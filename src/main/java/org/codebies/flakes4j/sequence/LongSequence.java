package org.codebies.flakes4j.sequence;

import org.codebies.flakes4j.Segment;
import org.codebies.flakes4j.TickChangeEventListener;
import org.codebies.flakes4j.TimerSegment;
import org.codebies.flakes4j.exception.InvalidSequenceConfigException;
import org.codebies.flakes4j.supplier.SegmentSupplier;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LongSequence implements Sequence<Long> {

    private final ReentrantLock mutex = new ReentrantLock();
    private final TimerSegment timerSegment;
    private final List<Segment> segments;

    private LongSequence(SequenceTemplate sequenceTemplate) {
       this.timerSegment = (TimerSegment) sequenceTemplate.getTimerSegment().newInstance();
       this.segments = sequenceTemplate.getSegmentSuppliers().stream().map(SegmentSupplier::newInstance).collect(Collectors.toList());
       validate(timerSegment, segments);
       registerListeners(timerSegment);
    }

    private void validate(TimerSegment timerSegment, List<Segment> segments){
        Objects.requireNonNull(timerSegment);
        Objects.requireNonNull(segments);

        int size = timerSegment.size();
        size += segments.stream().mapToInt(Segment::size).sum();
        if(size > 63){
            throw new InvalidSequenceConfigException("Long sequence should not allowed to exceed more than 63 bits");
        }
    }

    public static Sequence<Long> of(SequenceTemplate config){
        return new LongSequence(config);
    }

    private void registerListeners(TimerSegment timerSegment) {
        this.segments.stream()
                .filter(segment -> segment instanceof TickChangeEventListener)
                .map(segment -> (TickChangeEventListener)segment)
                .forEach(timerSegment::register);
    }

    public synchronized Long next() {
        mutex.lock();
        try {
            return generateNext();
        }finally {
            mutex.unlock();
        }
    }

    private long generateNext(){
        long sequence = timerSegment.next();
        for(Segment segment: segments){
            sequence = (sequence << segment.size()) | segment.next();
        }
        return sequence;
    }

}
