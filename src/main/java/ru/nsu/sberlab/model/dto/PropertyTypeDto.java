package ru.nsu.sberlab.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PropertyTypeDto {
    @JsonProperty(value = "property_value")
    private final String propertyValue;

    @JsonProperty(value = "property_id")
    private final Long propertyId;
}
