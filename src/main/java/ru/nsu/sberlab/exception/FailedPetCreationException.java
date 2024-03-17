package ru.nsu.sberlab.exception;

public class FailedPetCreationException extends RuntimeException {
    public FailedPetCreationException(String message) {
        super(message);
    }
}
