package org.codebies.flakes4j.sequence;

import java.time.LocalDate;
import java.time.ZoneOffset;

final class DateUtils {

    private static final long DATE_OFFSET;

    static {
        DATE_OFFSET = calculateOffset();
    }

    private static long calculateOffset() {
        return LocalDate.of(2021, 1, 1)
                .atStartOfDay()
                .atZone(ZoneOffset.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    static long getMillis() {
        return System.currentTimeMillis() - DATE_OFFSET;
    }


}
