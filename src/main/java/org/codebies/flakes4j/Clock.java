package org.codebies.flakes4j;

import org.codebies.flakes4j.utils.ClockUtils;

import java.time.temporal.Temporal;

public class Clock {

    private final long offset;

    private Clock(long offset){
        this.offset = offset;
    }

    public static Clock of(Temporal offset){
        return new Clock(ClockUtils.toMillis(offset));
    }

    public long millis() {
        return System.currentTimeMillis() - offset;
    }

}
