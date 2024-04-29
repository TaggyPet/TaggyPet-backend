package ru.nsu.sberlab.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.nsu.sberlab.model.enums.Sex;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetCreationDto {
    @JsonProperty(value = "chip_id")
    @Length(min=15, max=15)
    private String chipId;

    @JsonProperty(value = "stamp_id")
    private String stampId;

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
}
