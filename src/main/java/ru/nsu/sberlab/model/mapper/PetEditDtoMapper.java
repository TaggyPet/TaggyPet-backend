package ru.nsu.sberlab.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.sberlab.model.dto.PetEditDto;
import ru.nsu.sberlab.model.dto.PetImageDto;
import ru.nsu.sberlab.model.entity.Feature;
import ru.nsu.sberlab.model.entity.Pet;
import ru.nsu.sberlab.model.entity.FeatureProperty;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PetEditDtoMapper implements BiFunction<Pet, List<FeatureProperty>, PetEditDto> {
    private final FeatureCreationDtoMapper featureCreationDtoMapper;

    @Override
    public PetEditDto apply(Pet pet, List<FeatureProperty> properties) {
        List<Feature> features = pet.getFeatures();
        Map<Long, String> propertyMap = features
                .stream()
                .collect(Collectors.toMap(
                        feature -> feature.getProperty().getPropertyId(), Feature::getDescription, (a, b) -> b)
                );
        IntStream.range(0, properties.size())
                .filter(index -> !propertyMap.containsKey((long) index))
                .forEach(index -> features.add(index, new Feature(properties.get(index))));
        return new PetEditDto(
                pet.getName(),
                pet.getType(),
                pet.getBreed(),
                pet.getSex(),
                features
                        .stream()
                        .map(featureCreationDtoMapper)
                        .sorted(Comparator.naturalOrder())
                        .toList(),
                new PetImageDto(pet.getPetImage().getImageUUIDName())
        );
    }
}
