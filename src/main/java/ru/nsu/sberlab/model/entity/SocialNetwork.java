package ru.nsu.sberlab.model.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "social_networks")
@Data
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
