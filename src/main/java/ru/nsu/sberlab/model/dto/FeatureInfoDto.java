package ru.nsu.sberlab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeatureInfoDto implements Comparable<FeatureInfoDto> {
    private String description;
    private PropertyTypeDto propertyType;

    @Override
    public int compareTo(FeatureInfoDto other) {
        return Long.compare(this.getPropertyType().getPropertyId(), other.getPropertyType().getPropertyId());
    }
}
