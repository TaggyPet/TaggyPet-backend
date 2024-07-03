package ru.nsu.sberlab.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.sberlab.model.entity.FeatureProperty;

@Repository
public interface FeaturePropertiesRepository extends JpaRepository<FeatureProperty, Long> {
}
