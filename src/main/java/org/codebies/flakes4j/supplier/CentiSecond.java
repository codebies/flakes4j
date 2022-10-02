package org.codebies.flakes4j.supplier;

import io.vavr.control.Option;
import org.codebies.flakes4j.Segment;
import org.codebies.flakes4j.TimerSegments;

import java.time.temporal.Temporal;

public class CentiSecond implements TimerSegmentSupplier{

    private final Option<Temporal> offset;

    private CentiSecond(){
        offset = Option.none();
    }

    private CentiSecond(Temporal offset){
        this.offset = Option.of(offset);
    }

    public static TimerSegmentSupplier of(){
        return new CentiSecond();
    }

    public static TimerSegmentSupplier of(Temporal temporal){
        return new CentiSecond(temporal);
    }

    @Override
    public Segment newInstance() {
        return offset.map(TimerSegments::newCentiSecondsSegment)
                .getOrElse(TimerSegments::newCentiSecondsSegment);
    }
}
