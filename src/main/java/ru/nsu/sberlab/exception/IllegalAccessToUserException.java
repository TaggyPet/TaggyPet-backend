package ru.nsu.sberlab.exception;

public class IllegalAccessToUserException extends RuntimeException {
    public IllegalAccessToUserException(String message) {
        super(message);
    }
}
