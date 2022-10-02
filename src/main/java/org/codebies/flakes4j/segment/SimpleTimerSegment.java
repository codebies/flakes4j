package org.codebies.flakes4j.segment;

import org.codebies.flakes4j.AbstractTimerSegment;
import org.codebies.flakes4j.Clock;
import org.codebies.flakes4j.utils.ClockUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.function.Function;

public final class SimpleTimerSegment extends AbstractTimerSegment {

    private final int size;
    private final Function<Long,Long> timeExtractor;

    private SimpleTimerSegment(Clock clock, int size, Function<Long, Long> timeExtractor){
        super(clock);
        this.size = size;
        this.timeExtractor = timeExtractor;
    }

    public static SimpleTimerSegment of(int size, Function<Long, Long> timeExtractor){
        return new SimpleTimerSegment(ClockUtils.DEFAULT_CLOCK, size, timeExtractor);
    }

    public static SimpleTimerSegment of(Temporal offset, int size, Function<Long, Long> timeExtractor){
        return new SimpleTimerSegment(Clock.of(offset), size, timeExtractor);
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public long extractTime(long millis) {
        return  timeExtractor.apply(millis);
    }
}
