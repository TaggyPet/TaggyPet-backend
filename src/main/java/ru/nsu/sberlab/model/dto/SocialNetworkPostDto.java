package ru.nsu.sberlab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialNetworkPostDto {
    private Long propertyId;
    private String shortName;
}
