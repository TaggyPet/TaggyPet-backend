package ru.nsu.sberlab.exception;

public class FailedUserCreationException extends RuntimeException {
    private final String message;

    public FailedUserCreationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
