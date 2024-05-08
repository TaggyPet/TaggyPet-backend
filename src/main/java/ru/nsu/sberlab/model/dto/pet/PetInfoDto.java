package ru.nsu.sberlab.model.dto.pet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.nsu.sberlab.model.dto.feature.FeatureInfoDto;
import ru.nsu.sberlab.model.dto.user.UserCardDto;
import ru.nsu.sberlab.model.enums.Sex;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class PetInfoDto {
    @JsonProperty(value = "chip_id")
    private final String chipId;

    @JsonProperty(value = "stamp_id")
    private final String stampId;

    private final String name;

    private final String type;

    private final String breed;

    private final Sex sex;

    private final List<UserCardDto> users;

    private final List<FeatureInfoDto> features;

    @JsonProperty(value = "pet_image")
    private final PetImageDto petImageDto;
}
