package ru.nsu.sberlab.controller.exception;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.nsu.sberlab.exception.FailedPetSearchException;
import ru.nsu.sberlab.exception.PetNotFoundException;

import java.time.LocalDateTime;
import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorController { // TODO: add handling of IllegalAccessToPetException
    private final PropertyResolverUtils propertyResolverUtils;

    @ExceptionHandler(value = FailedPetSearchException.class)
    @GetMapping
    public ResponseEntity<ErrorResponse> handleFailedSearch(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                                LocalDateTime.now(),
                        propertyResolverUtils.resolve("api.server.error.pet-not-found",
                                Locale.getDefault()) + " : " + exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(value = {PetNotFoundException.class, UsernameNotFoundException.class})
    @GetMapping
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(LocalDateTime.now(), propertyResolverUtils.resolve(exception.getMessage(), Locale.getDefault())));
    }

    @ExceptionHandler(value = Exception.class)
    @GetMapping
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(LocalDateTime.now(), propertyResolverUtils.resolve(exception.getMessage(), Locale.getDefault())));
    }
}
