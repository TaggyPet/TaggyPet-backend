package ru.nsu.sberlab.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
public class ReCaptchaException extends RuntimeException {
    private final String redirectURI;

    @Getter
    private final List<String> errors;

    public String getRedirectURI() {
        if (redirectURI.isEmpty() || redirectURI.charAt(0) != '/') {
            return "/".concat(redirectURI);
        }
        return redirectURI;
    }
}
