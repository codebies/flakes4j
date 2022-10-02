package org.codebies.flakes4j.segment


import org.codebies.flakes4j.TimerSegments
import org.codebies.flakes4j.utils.ClockUtils
import spock.lang.Specification

class TimerSegmentTests extends Specification{

    void "verify the size of timer segment"(){
        given:
        def timerSegment = timer.call()

        expect:
        timerSegment.size() == size

        where:
        timer | size
        TimerSegments::newSecondsSegment | 32
        TimerSegments::newDeciSecondsSegment | 36
        TimerSegments::newCentiSecondsSegment | 39
        TimerSegments::newMilliSecondsSegment | 42
    }


    void "timer segment should roll on every tick"(){
        given:
        def timerSegment = timer.call();
        def result = [].toSet()
        def ticks = 3

        when:
        def currentTimer = tickfn.call(System.currentTimeMillis());
        def startTimer = currentTimer;
        while (startTimer + ticks >= currentTimer){
            if(currentTimer > startTimer){
                result.add(timerSegment.next());
            }
            currentTimer = tickfn.call(System.currentTimeMillis())
        }

        then:
        result.size() >= ticks -1 && result.size() <= ticks + delta


        where:
        timer | tickfn | delta
        TimerSegments::newSecondsSegment | ClockUtils::millisToSeconds || 1
        TimerSegments::newDeciSecondsSegment | ClockUtils::millisToDeciSeconds || 2
        TimerSegments::newCentiSecondsSegment | ClockUtils::millisToCentiSeconds || 4
        TimerSegments::newMilliSecondsSegment | ((milli) -> milli) || 6
    }



}
