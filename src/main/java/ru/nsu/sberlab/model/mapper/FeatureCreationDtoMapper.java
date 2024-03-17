package ru.nsu.sberlab.model.mapper;

import org.springframework.stereotype.Service;
import ru.nsu.sberlab.model.dto.FeatureCreationDto;
import ru.nsu.sberlab.model.entity.Feature;

import java.util.function.Function;

@Service
public class FeatureCreationDtoMapper implements Function<Feature, FeatureCreationDto> {
    @Override
    public FeatureCreationDto apply(Feature feature) {
        return new FeatureCreationDto(feature.getDescription(), feature.getProperty().getPropertyId());
    }
}
