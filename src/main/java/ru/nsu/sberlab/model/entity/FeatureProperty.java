package ru.nsu.sberlab.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "feature_properties")
@Data
@NoArgsConstructor
public class FeatureProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long propertyId;

    @Column(name = "property_value")
    private String propertyValue;

    @OneToMany(mappedBy = "property")
    private List<Feature> features = new ArrayList<>();
}
