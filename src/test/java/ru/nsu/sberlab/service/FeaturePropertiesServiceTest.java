package ru.nsu.sberlab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nsu.sberlab.dao.FeaturePropertiesRepository;
import ru.nsu.sberlab.model.entity.FeatureProperty;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FeaturePropertiesServiceTest {
    private FeaturePropertiesService featurePropertiesService;
    @Mock
    private FeaturePropertiesRepository featurePropertiesRepository;

    @BeforeEach
    void setUp() {
        featurePropertiesService = new FeaturePropertiesService(featurePropertiesRepository);
    }

    @Test
    void itShouldReturnEmptyListOfFeatureProperties() {
        given(featurePropertiesRepository.findAll())
                .willReturn(Collections.emptyList());

        List<FeatureProperty> featureProperties = featurePropertiesService.properties();

        verify(featurePropertiesRepository).findAll();
        assertThat(featureProperties).isEmpty();
    }
}
