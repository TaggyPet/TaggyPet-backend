package ru.nsu.sberlab.exception;

public class PreviousPasswordMatchesWithNewPasswordException extends RuntimeException {
    public PreviousPasswordMatchesWithNewPasswordException(String message) {
        super(message);
    }
}
