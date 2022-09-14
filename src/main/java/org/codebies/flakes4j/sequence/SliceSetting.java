package org.codebies.flakes4j.sequence;

import java.util.function.Function;

final class SliceSetting {

    private final int timeLength;
    private final int serialLength;
    private final int idLength;
    private final Function<Long,Long> timeFunction;

    private SliceSetting(int timeLength, int serialLength, int idLength, Function<Long, Long> timeFunction) {
        this.timeLength = timeLength;
        this.serialLength = serialLength;
        this.idLength = idLength;
        this.timeFunction = timeFunction;
    }

    public static SliceSetting centiSeconds() {
        return new SliceSetting(40, 8, 16, milliSeconds -> milliSeconds/10);
    }

    public static SliceSetting seconds() {
        return new SliceSetting(32, 16, 16, milliSeconds -> milliSeconds/1000);
    }

    public int getTimeLength() {
        return timeLength;
    }

    public int getSerialLength() {
        return serialLength;
    }

    public int getIdLength() {
        return idLength;
    }

    public Function<Long, Long> getTimeFunction() {
        return timeFunction;
    }
}
