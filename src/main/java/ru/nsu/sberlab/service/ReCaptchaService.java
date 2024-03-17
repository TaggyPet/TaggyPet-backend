package ru.nsu.sberlab.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nsu.sberlab.exception.ReCaptchaException;
import ru.nsu.sberlab.recaptcha.ReCaptchaKeys;
import ru.nsu.sberlab.recaptcha.ReCaptchaResponse;

import java.net.URI;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReCaptchaService {
    private final ReCaptchaKeys reCaptchaKeys;

    public void verify(String response, String request, @NonNull String redirectURI) {
        RestTemplate restTemplate = new RestTemplate();
        URI requestURI = URI.create(
                String.format(
                        reCaptchaKeys.getVerifyURI(),
                        reCaptchaKeys.getSecret(),
                        response
                )
        );
        ReCaptchaResponse reCaptchaResponse = restTemplate.getForObject(requestURI, ReCaptchaResponse.class);
        if (Objects.nonNull(reCaptchaResponse) && (!reCaptchaResponse.isSuccess() ||
                reCaptchaResponse.getScore() < reCaptchaKeys.getThreshold() ||
                !reCaptchaResponse.getAction().equals(request))) {
            throw new ReCaptchaException(redirectURI, reCaptchaResponse.getErrors());
        }
    }
}
