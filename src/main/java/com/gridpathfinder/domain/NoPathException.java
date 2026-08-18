package com.gridpathfinder.domain;

public class NoPathException extends RuntimeException {
    public NoPathException(String message) {
        super(message);
    }
}
