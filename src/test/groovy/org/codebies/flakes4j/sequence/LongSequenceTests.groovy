package org.codebies.flakes4j.sequence

import org.codebies.flakes4j.exception.InvalidInputException
import org.codebies.flakes4j.exception.InvalidSequenceConfigException
import org.codebies.flakes4j.utils.NumberUtils
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.IntStream

class LongSequenceTests extends Specification{


    void "exception should be thrown on exceeded bit size"(){
        when:
        def template = SequenceTemplate
                .withSecond()
                .constant(20, 25)
                .serial(12)
                .build();
        LongSequence.of(template);

        then:
        thrown(InvalidSequenceConfigException)
    }

    void "exception should be thrown on exceeded value allowed"(){
        given:
        def template = SequenceTemplate
                .withSecond()
                .constant(4, 20)
                .serial(12)
                .build();
        def sequence = LongSequence.of(template);

        when:
        sequence.next()

        then:
        thrown(InvalidInputException)
    }

    void "should allow to create sequence with maximum allowed bits"(){
        when:
        def template = SequenceTemplate
                .withSecond()
                .constant(20, 25)
                .serial(11)
                .build();
        LongSequence.of(template);

        then:
        noExceptionThrown()
    }

    void "should allow to create sequence with allowed bits"(){
        when:
        def template = SequenceTemplate
                .withSecond()
                .constant(5, 25)
                .serial(15)
                .build();
        LongSequence.of(template);

        then:
        noExceptionThrown()
    }


    void "constant bits should be placed on appropriate place"(){
        given:
        def template = SequenceTemplate
                .withSecond()
                .constant(5, 25)
                .serial(15)
                .build();
        def sequence = LongSequence.of(template);

        when:
        def result = sequence.next();

        then:
        NumberUtils.extract(result,45,49) == 25
    }


    void "constant bits should never be changed"(){
        given:
        def template = SequenceTemplate
                .withSecond()
                .constant(5, 25)
                .serial(15)
                .build();
        def sequence = LongSequence.of(template);
        def result = [].toSet()

        when:
        for (i in 1..500)
            result.add(NumberUtils.extract(sequence.next(),45,49));

        then:
        result.size() == 1
        result[0] == 25
    }


    void "unique sequence should be generated - single threaded"(){
        given:
        def template = SequenceTemplate
                .withSecond()
                .constant(5, 25)
                .serial(15)
                .build();
        def sequence = LongSequence.of(template);
        def result = [].toSet()

        when:
        def size = Math.pow(2,15) as int;
        for(i in 1..size)
            result.add(sequence.next());

        then:
        result.size() == size
    }


    void "unique sequence should be generated - single row - multi-threaded  #iteration"(){
        given:
        def template = SequenceTemplate
                .withSecond()
                .constant(5, 25)
                .serial(15)
                .build();
        def sequence = LongSequence.of(template);
        def result = [].toSet().asSynchronized() as Set<Long>;

        when:
        def size = Math.pow(2,15) as int;
        def users = 750;
        def request = (size / 750) as int;
        executeThreadedCycle(sequence, result, users, request);

        then:
        (users * request) == result.size()

        where:
        iteration << IntStream.range(0,100).toArray()
    }


    void "unique sequence should be generated - multi row - multi-threaded"(){
        given:
        def template = SequenceTemplate
                .withSecond()
                .constant(5, 25)
                .serial(15)
                .build();
        def sequence = LongSequence.of(template);
        def result = [].toSet().asSynchronized() as Set<Long>;

        when:
        def size = Math.pow(2,15) as int;
        def users = 750;
        def request = (size / 750) as int;
        def seconds = 30
        for (i in 1..seconds) {
            executeThreadedCycle(sequence, result, users, request);
            Thread.sleep(1000)
        }
        println users * request * seconds

        then:
        (users * request * seconds) == result.size()
    }


    void "unique sequence should be generated - multi row - multi-threaded #bitLength"(){
        given:
        def bSize = bitLength as int
        def template = SequenceTemplate
                .withSecond()
                .serial(bSize)
                .build();
        def sequence = LongSequence.of(template);
        def result = [].toSet().asSynchronized() as Set<Long>;

        when:
        def size = Math.pow(2,bSize) as int;
        def users = bSize;
        def request = (size / users) as int;
        executeThreadedCycle(sequence, result, users, request);

        then:
        (users * request) == result.size()

        where:
        bitLength << IntStream.range(1,25).toArray()
    }



    void executeThreadedCycle(Sequence<Long> idGenerator, Set<Long> ids, int users, int request) throws InterruptedException {
        def counter = new AtomicInteger(0);
        def count = users * request;
        for (i in 1..users){
            new Thread({
                for(j in 1..request) {
                    ids.add(idGenerator.next())
                    counter.incrementAndGet();
                }
            }).start()
        }
       while (counter.get() < count) {
            Thread.sleep(3);
        }
    }

}
