package org.codebies.flakes4j;

import org.codebies.flakes4j.segment.SimpleTimerSegment;
import org.codebies.flakes4j.utils.ClockUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.function.Function;

public interface TimerSegments extends Segment, Listenable {

    static TimerSegment newSecondsSegment(){
        return SimpleTimerSegment.of(32, ClockUtils::millisToSeconds);
    }

    static TimerSegment newDeciSecondsSegment(){
        return SimpleTimerSegment.of(36, ClockUtils::millisToDeciSeconds);
    }

    static TimerSegment newCentiSecondsSegment(){
        return SimpleTimerSegment.of(39, ClockUtils::millisToCentiSeconds);
    }

    static TimerSegment newMilliSecondsSegment(){
        return SimpleTimerSegment.of(42, Function.identity());
    }


    static TimerSegment newSecondsSegment(Temporal temporal){
        return SimpleTimerSegment.of(temporal,32, ClockUtils::millisToSeconds);
    }

    static TimerSegment newDeciSecondsSegment(Temporal temporal){
        return SimpleTimerSegment.of(temporal,36, ClockUtils::millisToDeciSeconds);
    }

    static TimerSegment newCentiSecondsSegment(Temporal temporal){
        return SimpleTimerSegment.of(temporal,39, ClockUtils::millisToCentiSeconds);
    }

    static TimerSegment newMilliSecondsSegment(Temporal temporal){
        return SimpleTimerSegment.of(temporal,42, Function.identity());
    }

}
