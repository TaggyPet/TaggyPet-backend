package ru.nsu.sberlab.api.exception;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, String message) {
}