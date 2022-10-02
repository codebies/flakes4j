package org.codebies.flakes4j.segment

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.codebies.flakes4j.Segments
import org.codebies.flakes4j.exception.InvalidInputException
import spock.lang.Specification

import java.util.function.Supplier
import java.util.stream.IntStream

@CompileStatic
class SimpleSegments$ConstantSegmentTests extends Specification{

    @CompileDynamic
    void "Verify exception on constant segment with zero or negative bit length #bits"(){
        when:
        Segments.newConstantSegment(bits, 2)

        then:
        thrown(InvalidInputException)

        where:
        bits | _
        0 | _
        -1 | _
    }

    void "Verify exception on constant segment with exceeded allowed bit length"(){
        when:
        Segments.newConstantSegment(32, 2)

        then:
        thrown(InvalidInputException)
    }

    @CompileDynamic
    void "Verify exception on constant segment with negative value"(){
        given:
        def segment = Segments.newConstantSegment(bits, nValue)

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

    @CompileDynamic
    void "Verify exception on constant segment with exceeded value"(){
        given:
        def segment = Segments.newConstantSegment(bits, invalidValue)

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


    @CompileDynamic
    void "Verify constant segment validity with valid bit length #bitLength"(){
        when:
        Segments.newConstantSegment(bitLength as int, 400)

        then:
        noExceptionThrown()

        where:
        bitLength << IntStream.range(1,32).toArray()
    }

    @CompileDynamic
    void "Verify constant segment validity with zero as value and bit length #bits"(){
        given:
        def segment = Segments.newConstantSegment(bits as int, 0)

        when:
        segment.next()

        then:
        noExceptionThrown()

        where:
        bits << IntStream.range(1,32).toArray()
    }

    @CompileDynamic
    void "Verify constant segment validity with max value  #bits - #maxValue"(){
        given:
        def segment = Segments.newConstantSegment(bits as int, maxValue as int)

        when:
        segment.next()

        then:
        noExceptionThrown()

        where:
        bits << IntStream.range(1,32).toArray();
        maxValue << IntStream.range(1,32).map({Math.pow(2,it)-1 as int}).toArray()
    }


    void "constant segment should generate the constant value regardless of number of request"(){
        given:
        def value = new Random().nextInt(31);
        def segment = Segments.newConstantSegment(5,value)
        def set = [].toSet()

        when:
        for (i in 1..100)
            set.add(segment.next())

        then:
        set.size() == 1
    }


    void "constant segment should generate the constant value that is configured"(){
        given:
        def value = new Random().nextInt(255);
        def segment = Segments.newConstantSegment(8,value)

        expect:
        segment.next() == value
    }

    /**
     *  Supplier
     */

    @CompileDynamic
    void "Verify exception on constant supplier segment with zero or negative bit length #bits"(){
        when:
        Segments.newConstantSegment(bits, ()->2)

        then:
        thrown(InvalidInputException)

        where:
        bits | _
        0 | _
        -1 | _
    }

    void "Verify exception on constant supplier segment with exceeded allowed bit length"(){
        when:
        Segments.newConstantSegment(32, ()->2)

        then:
        thrown(InvalidInputException)
    }

    void "Verify exception on constant supplier segment with null value"(){
        when:
        Segments.newConstantSegment(32, ()->null)

        then:
        thrown(InvalidInputException)
    }

    @CompileDynamic
    void "Verify exception on constant supplier segment with negative value"(){
        given:
        def segment = Segments.newConstantSegment(bits, ()->nValue)

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

    @CompileDynamic
    void "Verify exception on constant supplier segment with exceeded value"(){
        given:
        def segment = Segments.newConstantSegment(bits, ()->invalidValue)

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


    @CompileDynamic
    void "Verify constant supplier segment validity with valid bit length #bitLength"(){
        when:
        Segments.newConstantSegment(bitLength as int, ()->400)

        then:
        noExceptionThrown()

        where:
        bitLength << IntStream.range(1,32).toArray()
    }

    @CompileDynamic
    void "Verify constant supplier segment validity with zero as value and bit length #bits"(){
        given:
        def segment = Segments.newConstantSegment(bits as int, ()->0)

        when:
        segment.next()

        then:
        noExceptionThrown()

        where:
        bits << IntStream.range(1,32).toArray()
    }

    @CompileDynamic
    void "Verify constant supplier segment validity with max value  #bits - #maxValue"(){
        given:
        def segment = Segments.newConstantSegment(bits as int, ()->maxValue as int)

        when:
        segment.next()

        then:
        noExceptionThrown()

        where:
        bits << IntStream.range(1,32).toArray();
        maxValue << IntStream.range(1,32).map({Math.pow(2,it)-1 as int}).toArray()
    }


    void "constant supplier segment should generate the constant value regardless of number of request"(){
        given:
        def value = new Random().nextInt(31);
        def segment = Segments.newConstantSegment(5,()->value)
        def set = [].toSet()

        when:
        for (i in 1..100)
            set.add(segment.next())

        then:
        set.size() == 1
    }


    void "constant supplier segment should generate the constant value that is configured"(){
        given:
        def value = new Random().nextInt(255);
        def segment = Segments.newConstantSegment(8,()->value)

        expect:
        segment.next() == value
    }

    void "constant supplier segment should not call the supplier more than once"(){
        given:
        def value = 200
        def supplier = Mock(Supplier)
        def segment = Segments.newConstantSegment(8, supplier)

        when:
        for (i in 1..10)
            segment.next()

        then: "Expected only one time invokation"
        1 * supplier.get() >> value
    }


}
