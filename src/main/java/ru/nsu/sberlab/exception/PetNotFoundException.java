package ru.nsu.sberlab.exception;

public class PetNotFoundException extends RuntimeException {
    private final String message;

    public PetNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
