package org.codebies.flakes4j.exception;

public class SequenceLimitExceeded extends RuntimeException {

    public SequenceLimitExceeded(String message) {
        super(message);
    }
}
