package ru.nsu.sberlab.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "social_networks")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class SocialNetwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long propertyId;

    @Column(name = "property_name")
    private String propertyValue;

    @Column(name = "base_url")
    private String baseUrl;
}
