package ru.nsu.sberlab.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "features")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_id")
    private Long featureId;

    @Column(name = "date")
    private LocalDate dateTime;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "features")
    @EqualsAndHashCode.Exclude
    private List<Pet> pets = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_id")
    private FeatureProperty property;

    @ManyToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private User user;

    public Feature(FeatureProperty property) {
        this.property = property;
    }

    public Feature(String description, FeatureProperty property, User principal) {
        this.description = description;
        this.property = property;
        this.user = principal;
    }

    @PrePersist
    private void initialization() {
        this.dateTime = LocalDate.now();
    }
}
