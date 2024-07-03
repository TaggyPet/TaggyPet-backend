package ru.nsu.sberlab.api.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.sberlab.model.dto.user.UserRegistrationDto;
import ru.nsu.sberlab.service.UserService;

@RestController
@RequiredArgsConstructor
@Tag(name = "security", description = "API для работы с аутентификацией и регистрации пользователя")
@RequestMapping("api/v1/security")
public class SecurityController {
    private final UserService userService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/registration")
    public ResponseEntity<?> createUser(@RequestBody @Validated UserRegistrationDto user) {
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
