package ru.nsu.sberlab.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class FeatureInfoDto implements Comparable<FeatureInfoDto> {
    private String description;

    @JsonProperty(value = "property_type")
    private PropertyTypeDto propertyType;

    @Override
    public int compareTo(FeatureInfoDto other) {
        return Long.compare(this.getPropertyType().getPropertyId(), other.getPropertyType().getPropertyId());
    }
}
