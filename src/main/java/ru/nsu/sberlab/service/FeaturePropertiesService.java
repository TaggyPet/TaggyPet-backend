package ru.nsu.sberlab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.sberlab.model.entity.FeatureProperty;
import ru.nsu.sberlab.dao.FeaturePropertiesRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeaturePropertiesService {
    private final FeaturePropertiesRepository featurePropertiesRepository;

    public List<FeatureProperty> properties() {
        return featurePropertiesRepository.findAll();
    }
}
