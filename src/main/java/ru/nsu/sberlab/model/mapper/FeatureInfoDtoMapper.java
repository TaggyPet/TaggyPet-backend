package ru.nsu.sberlab.model.mapper;

import org.springframework.stereotype.Service;
import ru.nsu.sberlab.model.dto.FeatureInfoDto;
import ru.nsu.sberlab.model.dto.PropertyTypeDto;
import ru.nsu.sberlab.model.entity.Feature;

import java.util.function.Function;

@Service
public class FeatureInfoDtoMapper implements Function<Feature, FeatureInfoDto> {
    @Override
    public FeatureInfoDto apply(Feature feature) {
        return new FeatureInfoDto(
                feature.getDescription(),
                new PropertyTypeDto(feature.getProperty().getPropertyValue(), feature.getProperty().getPropertyId())
        );
    }
}
