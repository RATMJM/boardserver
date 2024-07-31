package com.boardTest.boardServer.exception;

public class DuplicateIdException extends RuntimeException {
    public DuplicateIdException(String msg) {
        super(msg);
    }
}
