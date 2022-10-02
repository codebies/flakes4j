package org.codebies.flakes4j.supplier;

import io.vavr.control.Option;
import org.codebies.flakes4j.Segment;
import org.codebies.flakes4j.TimerSegments;

import java.time.temporal.Temporal;

public class DeciSecond implements TimerSegmentSupplier{

    private final Option<Temporal> offset;

    private DeciSecond(){
        offset = Option.none();
    }

    private DeciSecond(Temporal offset){
        this.offset = Option.of(offset);
    }

    public static TimerSegmentSupplier of(){
        return new DeciSecond();
    }

    public static TimerSegmentSupplier of(Temporal temporal){
        return new DeciSecond(temporal);
    }

    @Override
    public Segment newInstance() {
        return offset.map(TimerSegments::newDeciSecondsSegment)
                .getOrElse(TimerSegments::newDeciSecondsSegment);
    }
}
