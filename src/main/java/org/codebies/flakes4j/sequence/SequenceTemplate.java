package org.codebies.flakes4j.sequence;

import org.codebies.flakes4j.supplier.*;

import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public final class SequenceTemplate {

    private final SegmentSupplier timerSegment;
    private final List<SegmentSupplier> segmentSuppliers;

    public SequenceTemplate(SegmentSupplier timerSegment, List<SegmentSupplier> segmentSuppliers) {
        this.timerSegment = timerSegment;
        if(segmentSuppliers==null)
            this.segmentSuppliers = Collections.emptyList();
        else
            this.segmentSuppliers = segmentSuppliers;
    }

    public SegmentSupplier getTimerSegment() {
        return timerSegment;
    }

    public List<SegmentSupplier> getSegmentSuppliers() {
        return segmentSuppliers;
    }

    public static Builder withSecond(){
        return new Builder(Second.of());
    }

    public static Builder withDeciSecond(){
        return new Builder(DeciSecond.of());
    }

    public static Builder withCentiSecond(){
        return new Builder(CentiSecond.of());
    }

    public static Builder withMilliSecond(){
        return new Builder(MilliSecond.of());
    }


    public static Builder withSecond(Temporal offset){
        return new Builder(Second.of(offset));
    }

    public static Builder withDeciSecond(Temporal offset){
        return new Builder(DeciSecond.of(offset));
    }

    public static Builder withCentiSecond(Temporal offset){
        return new Builder(CentiSecond.of(offset));
    }

    public static Builder withMilliSecond(Temporal offset){
        return new Builder(MilliSecond.of(offset));
    }

    public static class Builder{

        private final SegmentSupplier timerSegment;
        private final List<SegmentSupplier> segmentSuppliers = new LinkedList<>();

        private Builder(TimerSegmentSupplier timerSegmentSupplier){
            this.timerSegment = timerSegmentSupplier;
        }

        public Builder constant(int size, int value){
            segmentSuppliers.add(Constant.of(size,value));
            return this;
        }

        public Builder constant(int size, Supplier<Integer> supplier){
            segmentSuppliers.add(Constant.of(size,supplier));
            return this;
        }

        public Builder randomOnce(int size){
            segmentSuppliers.add(RandomOnce.of(size));
            return this;
        }

        public Builder random(int size){
            segmentSuppliers.add(Random.of(size));
            return this;
        }

        public Builder random(int size, Supplier<Integer> value){
            segmentSuppliers.add(Random.of(size,value));
            return this;
        }

        public Builder serial(int size){
            segmentSuppliers.add(Serial.of(size));
            return this;
        }

        public Builder add(ValueSegmentSupplier segmentSupplier){
            segmentSuppliers.add(segmentSupplier);
            return this;
        }

        public SequenceTemplate build(){
            return new SequenceTemplate(timerSegment,segmentSuppliers);
        }

    }

}
