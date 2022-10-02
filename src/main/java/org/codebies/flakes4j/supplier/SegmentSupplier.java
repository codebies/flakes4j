package org.codebies.flakes4j.supplier;

import org.codebies.flakes4j.Segment;

@FunctionalInterface
public interface SegmentSupplier {
    Segment newInstance();

}
