package org.codebies.flakes4j.segment

import org.codebies.flakes4j.Segments
import org.codebies.flakes4j.exception.InvalidInputException
import spock.lang.Specification

import java.util.function.Supplier
import java.util.stream.IntStream

class SimpleSegments$RandomSegmentTests extends Specification{

    void "Verify exception on random  segment with zero or negative bit length #bits"(){
        when:
        Segments.newRandomSegment(bits)

        then:
        thrown(InvalidInputException)

        where:
        bits | _
        0 | _
        -1 | _
    }

    void "Verify exception on random  segment with exceeded allowed bit length"(){
        when:
        Segments.newRandomSegment(32)

        then:
        thrown(InvalidInputException)
    }


    void "Verify random segment validity with valid bit length #bitLength"(){
        when:
        Segments.newRandomSegment(bitLength as int)

        then:
        noExceptionThrown()

        where:
        bitLength << IntStream.range(1,32).toArray()
    }

    void "random segment should generate the random number atlease 75 %"(){
        given:
        def segment = Segments.newRandomSegment(8)
        def result = [].toSet()

        when:
        for (i in 1..100)
            result.add(segment.next());

        then:
        75 <= result.size()
    }


    /**
     * Supplier
     */

    void "Verify exception on random supplier segment with zero or negative bit length #bits"(){
        when:
        Segments.newRandomSegment(bits, ()->2)

        then:
        thrown(InvalidInputException)

        where:
        bits | _
        0 | _
        -1 | _
    }

    void "Verify exception on random supplier segment with exceeded allowed bit length"(){
        when:
        Segments.newRandomSegment(32, ()->2)

        then:
        thrown(InvalidInputException)
    }

    void "Verify exception on random supplier segment with null value"(){
        when:
        Segments.newRandomSegment(32, ()->null)

        then:
        thrown(InvalidInputException)
    }

    void "Verify exception on random supplier segment with negative value"(){
        given:
        def segment = Segments.newRandomSegment(bits, ()->nValue)

        when:
        segment.next()

        then:
        thrown(InvalidInputException)

        where:
        bits | nValue
        3 | -1
        2 | -20
        4 | -400
    }

    void "Verify exception on random supplier segment with exceeded value"(){
        given:
        def segment = Segments.newRandomSegment(bits, ()->invalidValue)

        when:
        segment.next()

        then:
        thrown(InvalidInputException)

        where:
        bits | invalidValue
        3 | 8
        3 | 15
        4 | 20
    }


    void "Verify random supplier segment validity with valid bit length #bitLength"(){
        when:
        Segments.newRandomSegment(bitLength as int, ()->400)

        then:
        noExceptionThrown()

        where:
        bitLength << IntStream.range(1,32).toArray()
    }

    void "Verify random supplier segment validity with zero as value and bit length #bits"(){
        given:
        def segment = Segments.newRandomSegment(bits as int, ()->0)

        when:
        segment.next()

        then:
        noExceptionThrown()

        where:
        bits << IntStream.range(1,32).toArray()
    }

    void "Verify random supplier segment validity with max value  #bits - #maxValue"(){
        given:
        def segment = Segments.newRandomSegment(bits as int, ()->maxValue as int)

        when:
        segment.next()

        then:
        noExceptionThrown()

        where:
        bits << IntStream.range(1,32).toArray();
        maxValue << IntStream.range(1,32).map({Math.pow(2,it)-1 as int}).toArray()
    }


    void "random supplier segment should generate the passed value"(){
        given:

        def segment = Segments.newRandomSegment(8,()->value)

        expect:
        segment.next() == value

        where:
        value << IntStream.range(0,100).map({new Random().nextInt(255)}).toArray();
    }

    void "random supplier segment should  call the supplier exactly once per call"(){
        given:
        def value = new Random().nextInt(255)
        def supplier = Mock(Supplier)
        def segment = Segments.newRandomSegment(8, supplier)

        when:
        for (i in 1..10)
            segment.next()
       
        then: "Expected only one time invokation"
        10 * supplier.get() >> value

    }


}
