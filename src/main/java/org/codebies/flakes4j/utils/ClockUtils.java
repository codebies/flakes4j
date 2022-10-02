package org.codebies.flakes4j.utils;

import org.codebies.flakes4j.Clock;
import org.codebies.flakes4j.exception.InvalidInputException;

import java.time.*;
import java.time.temporal.Temporal;

public final class ClockUtils {

    private static final ZoneId DEFAULT_ZONEID = ZoneId.of("UTC");
    public static final Clock DEFAULT_CLOCK = Clock.of(LocalDate.of(2022,9,1).atStartOfDay(DEFAULT_ZONEID));

    public static long toMillis(Temporal temporal){
        if(temporal instanceof ZonedDateTime){
            return toMillis((ZonedDateTime) temporal);
        }else if(temporal instanceof  LocalDateTime){
            return toMillis((LocalDateTime) temporal);
        }else if(temporal instanceof LocalDate){
            return toMillis((LocalDate) temporal);
        }else{
            throw new InvalidInputException();
        }
    }

    public static long toMillis(ZonedDateTime zonedDateTime) {
        return zonedDateTime
                .toInstant()
                .toEpochMilli();
    }

    public static long toMillis(LocalDateTime localDateTime){
        return toMillis(localDateTime.atZone(DEFAULT_ZONEID));
    }

    public static long toMillis(LocalDate localDate){
        return toMillis(localDate.atStartOfDay());
    }

    public static long millisToSeconds(long millis){
        return millis / 1000;
    }

    public static long millisToDeciSeconds(long millis){
        return millis / 100;
    }
    public static long millisToCentiSeconds(long millis){
        return millis / 10;
    }

}
