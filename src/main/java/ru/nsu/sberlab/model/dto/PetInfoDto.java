package ru.nsu.sberlab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.nsu.sberlab.model.enums.Sex;

import java.util.List;

@Data
@AllArgsConstructor
public class PetInfoDto {
    private final String chipId;
    private final String stampId;
    private final String name;
    private final String type;
    private final String breed;
    private final Sex sex;
    private final List<UserCardDto> users;
    private final List<FeatureInfoDto> features;
    private final PetImageDto petImageDto;
}
