package org.codebies.flakes4j.sequence;

public final class Sequences {

    public static Sequence<Long> newSecondsBasedSequence() {
        return new SimpleSequence(SliceSetting.seconds());
    }

    public static Sequence<Long> newSecondsBasedSequence(int instanceId){
        return new SimpleSequence(SliceSetting.centiSeconds(), instanceId);
    }

}
