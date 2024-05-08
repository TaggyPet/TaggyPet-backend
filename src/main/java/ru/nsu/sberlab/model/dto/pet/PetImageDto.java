package ru.nsu.sberlab.model.dto.pet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class PetImageDto {
    @JsonProperty(value = "image_uuid_name")
    private String imageUUIDName;
}
