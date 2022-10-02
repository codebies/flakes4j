package org.codebies.flakes4j.segment

import org.codebies.flakes4j.Segments
import org.codebies.flakes4j.exception.InvalidInputException
import spock.lang.Specification

import java.util.stream.IntStream

class SimpleSegments$RandomOnceSegmentTests extends Specification{

    void "Verify exception on randomOnce segment with zero or negative bit length #bits"(){
        when:
        Segments.newRandomOnceSegment(bits)

        then:
        thrown(InvalidInputException)

        where:
        bits | _
        0 | _
        -1 | _
    }

    void "Verify exception on randomOnce segment with exceeded allowed bit length"(){
        when:
        Segments.newRandomOnceSegment(32)

        then:
        thrown(InvalidInputException)
    }

    void "Verify randomOnce segment validity with valid bit length #bitLength"(){
        when:
        Segments.newRandomOnceSegment(bitLength as int)

        then:
        noExceptionThrown()

        where:
        bitLength << IntStream.range(1,32).toArray()
    }

    void "RandomOnce segment should generate the single random value regardless of number of request"(){
        given:
        def segment = Segments.newRandomOnceSegment(5)
        def set = [].toSet()

        when:
        for (i in 1..100)
            set.add(segment.next())

        then:
        set.size() == 1
    }


    void "RandomOnce segment should generate the number within given bit length #bits"(){
        given:
        def segment = Segments.newRandomOnceSegment(bits as int)

        expect:
        maxValue >= segment.next()

        where:
        bits << IntStream.range(1,31).toArray();
        maxValue << IntStream.range(1,31).map({Math.pow(2,it) as int}).toArray()
    }




}
