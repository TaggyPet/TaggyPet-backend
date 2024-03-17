package ru.nsu.sberlab.recaptcha;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "google.recaptcha.key")
public class ReCaptchaKeys {
    private String verifyURI;
    private String site;
    private String secret;
    private float threshold;
}
