package ru.nsu.sberlab.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureCreationDto implements Comparable<FeatureCreationDto> {
    // TODO: maybe use annotation @NotBlank
    @NotNull
    private String description;

    @JsonProperty(value = "property_id")
    private Long propertyId;

    @Override
    public int compareTo(FeatureCreationDto other) {
        return Long.compare(this.getPropertyId(), other.getPropertyId());
    }
}
