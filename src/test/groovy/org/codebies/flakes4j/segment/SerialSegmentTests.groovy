package org.codebies.flakes4j.segment

import org.codebies.flakes4j.Segment
import org.codebies.flakes4j.Segments
import org.codebies.flakes4j.exception.InvalidInputRangeException
import org.codebies.flakes4j.exception.SequenceLimitExceeded
import spock.lang.Specification

import java.util.stream.LongStream

class SerialSegmentTests extends Specification{

    void "serial segment with exceeded max limit should fail with InvalidInputRangeException"(){
        when:
        Segments.newSerialSegment(a);

        then:
        thrown(InvalidInputRangeException)

        where:
        a | _
        64 | _
        65 | _
        90 | _
    }


    void "serial segment with exceeded min limit should fail with InvalidInputRangeException"(){
        when:
        Segments.newSerialSegment(a);

        then:
        thrown(InvalidInputRangeException)

        where:
        a | _
        0 | _
        -1 | _
        -20 | _
    }


    void "serial segment should be allowed to created with max limit"(){
        when:
        Segments.newSerialSegment(63);

        then:
        noExceptionThrown()
    }

    void "serial segment should be allowed to created with min limit"(){
        when:
        Segments.newSerialSegment(1);

        then:
        noExceptionThrown()
    }

    void "verify the serial segment with minimum limit"(){
        given:
        def sequence = Segments.newSerialSegment(1);

        expect:
        sequence.next() == 0
    }

    void "verify min, max and count with the serial segment"(){
        given:
        def sequence = Segments.newSerialSegment(bitLength);

        when:
        def limit = Math.pow(2, bitLength) as long
        def statistics = LongStream.range(0, limit).map(i -> sequence.next()).summaryStatistics();

        then:
        statistics.min == 0
        statistics.max == limit - 1
        statistics.count == limit

        where:
        bitLength | _
        1 | _
        2 | _
        10 | _
        20 | _
    }


    void "serial segment should start with zero"(){
        given:
        def segment = Segments.newSerialSegment(10);

        expect:
        segment.next() == 0
    }

    void "serial segment should generate sequence in order"(){
        given:
        Segment segment = Segments.newSerialSegment(3);

        when:
        def sequences = LongStream.range(0, 8).map(index -> segment.next()).boxed().collect()

        then:
        sequences == [0,1,2,3,4,5,6,7]
    }

    void "serial segment should not generate more than limit and throw SequenceLimitExceeded"(){
        given:
        Segment segment = Segments.newSerialSegment(10);

        when:
        def sequences = LongStream.range(0, Math.pow(2,10)+1 as long)
                .map(index -> segment.next()).collect()

        then:
        thrown(SequenceLimitExceeded)
    }


    void "serial segment should reset on tick change Event"(){
        given:
        Segment segment = Segments.newSerialSegment(10);

        when:
        def sequences1 = LongStream.range(0, 100)
                .map(index -> segment.next()).collect()
        ((SerialSegment)segment).onTickChange()
        def sequences2 = LongStream.range(0,50)
                .map(index -> segment.next()).collect()
        sequences1.addAll(sequences2)

        then:
        def expected = LongStream.range(0, 150).map(v -> v % 100).collect()
        expected == sequences1
    }


}
