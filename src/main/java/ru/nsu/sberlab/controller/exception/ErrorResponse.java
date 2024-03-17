package ru.nsu.sberlab.controller.exception;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, String message) {
}