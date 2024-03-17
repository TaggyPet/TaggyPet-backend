package ru.nsu.sberlab.recaptcha;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class ReCaptchaResponse {
    private boolean success;
    private float score;
    private String action;
    private String hostname;

    @JsonProperty("challenge_ts")
    private String challengeTs;

    @JsonProperty("error-codes")
    private List<String> errorCodes;

    public List<String> getErrors() {
        if (Objects.nonNull(errorCodes)) {
            return errorCodes.stream()
                    .map(ReCaptchaErrorCodes.RECAPTCHA_ERROR_CODES::get)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
