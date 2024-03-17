package ru.nsu.sberlab.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PersonalCabinetDto {
    private final String firstName;
    private final String email;
    private final String phoneNumber;
    private final List<SocialNetworkOverviewDto> socialNetworks;
    private final List<PetCardDto> pets;
}
