package org.codebies.flakes4j.exception;

public class InvalidInputRangeException extends RuntimeException {

    public InvalidInputRangeException() {
    }

    public InvalidInputRangeException(String message) {
        super(message);
    }

    public InvalidInputRangeException(int start, int end) {
        super(String.format("Allowed Range [%d - %d]",start, end));
    }
}
