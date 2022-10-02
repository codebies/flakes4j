package org.codebies.flakes4j.supplier;

import io.vavr.control.Option;
import org.codebies.flakes4j.Segment;
import org.codebies.flakes4j.TimerSegments;

import java.time.temporal.Temporal;

public class Second implements TimerSegmentSupplier {

    private final Option<Temporal> offset;

    private Second(){
        offset = Option.none();
    }

    private Second(Temporal offset){
        this.offset = Option.of(offset);
    }

    public static TimerSegmentSupplier of(){
        return new Second();
    }

    public static TimerSegmentSupplier of(Temporal temporal){
        return new Second(temporal);
    }

    @Override
    public Segment newInstance() {
        return offset.map(TimerSegments::newSecondsSegment)
                .getOrElse(TimerSegments::newSecondsSegment);
    }
}
