package ru.nsu.sberlab.model.util;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.stereotype.Service;
import ru.nsu.sberlab.exception.PropertyNotFoundException;
import ru.nsu.sberlab.model.dto.FeatureCreationDto;
import ru.nsu.sberlab.model.entity.Feature;
import ru.nsu.sberlab.model.entity.User;
import ru.nsu.sberlab.dao.FeaturePropertiesRepository;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FeaturesConverter {
    private final FeaturePropertiesRepository featurePropertiesRepository;
    private final PropertyResolverUtils propertyResolver;

    public List<Feature> convertFeatureDtoListToFeatures(List<FeatureCreationDto> featureCreationDtoList, User user) {
        return featureCreationDtoList
                .stream()
                .filter(dto -> !dto.getDescription().isEmpty())
                .map(dto -> new Feature(
                                dto.getDescription(),
                                featurePropertiesRepository.findById(dto.getPropertyId()).orElseThrow(
                                        () -> new PropertyNotFoundException(
                                                message("api.server.error.property-not-found")
                                        )
                                ),
                                user
                        )
                )
                .toList();
    }

    private String message(String property) {
        return propertyResolver.resolve(property, Locale.getDefault());
    }
}
