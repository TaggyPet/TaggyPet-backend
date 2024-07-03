package ru.nsu.sberlab.api.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.sberlab.model.entity.User;

@RestController
@RequestMapping("api/v1/security")
public class SecurityController {

    @PostMapping("auth")
    public ResponseEntity<?> authenticate(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().build();
    }
}
