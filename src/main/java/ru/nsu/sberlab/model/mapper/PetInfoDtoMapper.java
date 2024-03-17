package ru.nsu.sberlab.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.sberlab.model.dto.PetImageDto;
import ru.nsu.sberlab.model.dto.PetInfoDto;
import ru.nsu.sberlab.model.entity.Pet;

import java.util.Comparator;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PetInfoDtoMapper implements Function<Pet, PetInfoDto> {
    private final UserCardDtoMapper userInfoMapper;
    private final FeatureInfoDtoMapper featureInfoDtoMapper;

    @Override
    public PetInfoDto apply(Pet pet) {
        return new PetInfoDto(
                pet.getChipId(),
                pet.getStampId(),
                pet.getName(),
                pet.getType(),
                pet.getBreed(),
                pet.getSex(),
                pet.getUsers()
                        .stream()
                        .map(userInfoMapper)
                        .toList(),
                pet.getFeatures()
                        .stream()
                        .map(featureInfoDtoMapper)
                        .sorted(Comparator.naturalOrder())
                        .toList(),
                new PetImageDto(pet.getPetImage().getImageUUIDName())
        );
    }
}
