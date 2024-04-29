package ru.nsu.sberlab.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SocialNetworkInfoDto {
    @JsonProperty(value = "property_id")
    private Long propertyId;

    @JsonProperty(value = "property_value")
    private String propertyValue;

    @JsonProperty(value = "short_name")
    private String shortName;
}
