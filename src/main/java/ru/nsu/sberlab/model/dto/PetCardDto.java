package ru.nsu.sberlab.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PetCardDto {
    @JsonProperty(value = "chip_id")
    private final String chipId;

    @JsonProperty(value = "stamp_id")
    private final String stampId;

    private final String name;

    @JsonProperty(value = "pet_image")
    private final PetImageDto petImageDto;
}
