package ru.nsu.sberlab.model.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.nsu.sberlab.model.dto.socialnetwork.SocialNetworkOverviewDto;
import ru.nsu.sberlab.model.dto.pet.PetCardDto;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PersonalCabinetDto {
    @JsonProperty(value = "first_name")
    private final String firstName;

    private final String email;

    @JsonProperty(value = "phone_number")
    private final String phoneNumber;

    @JsonProperty(value = "social_networks")
    private final List<SocialNetworkOverviewDto> socialNetworks;

    private final List<PetCardDto> pets;
}
