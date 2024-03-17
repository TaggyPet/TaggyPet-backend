package ru.nsu.sberlab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.sberlab.model.enums.Sex;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetEditDto {
    private String name;
    private String type;
    private String breed;
    private Sex sex;
    private List<FeatureCreationDto> features = new ArrayList<>();
    private PetImageDto petImageDto;
}
