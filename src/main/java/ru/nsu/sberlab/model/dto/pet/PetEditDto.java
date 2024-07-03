package ru.nsu.sberlab.model.dto.pet;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.nsu.sberlab.model.dto.feature.FeatureCreationDto;
import ru.nsu.sberlab.model.enums.Sex;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PetEditDto {
    @NotBlank
    private String name;

    @NotBlank
    @Length(min=3, max=100)
    private String type;

    @NotBlank
    @Length(min=3, max=100)
    private String breed;

    @NotNull
    private Sex sex;

    @Valid
    private List<FeatureCreationDto> features = new ArrayList<>();

    @JsonProperty(value = "pet_image")
    private PetImageDto petImageDto;
}
