package org.codebies.flakes4j.exception;

public class InvalidSegmentException extends RuntimeException {

    public InvalidSegmentException(){}

    public InvalidSegmentException(String message){
        super(message);
    }

}
