package org.codebies.flakes4j.supplier;

import io.vavr.control.Option;
import org.codebies.flakes4j.Segment;
import org.codebies.flakes4j.TimerSegments;

import java.time.temporal.Temporal;

public class MilliSecond implements TimerSegmentSupplier{

    private final Option<Temporal> offset;

    private MilliSecond(){
        offset = Option.none();
    }

    private MilliSecond(Temporal offset){
        this.offset = Option.of(offset);
    }

    public static TimerSegmentSupplier of(){
        return new MilliSecond();
    }

    public static TimerSegmentSupplier of(Temporal temporal){
        return new MilliSecond(temporal);
    }

    @Override
    public Segment newInstance() {
        return offset.map(TimerSegments::newMilliSecondsSegment)
                .getOrElse(TimerSegments::newMilliSecondsSegment);
    }
}
