package ru.nsu.sberlab.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class SocialNetworkLinkDto {
    private final String name;

    @JsonProperty(value = "base_url")
    private final String baseUrl;

    @JsonProperty(value = "short_name")
    private final String shortName;
}
