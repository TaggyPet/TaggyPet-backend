package ru.nsu.sberlab.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class UserCardDto {
    @JsonProperty(value = "first_name")
    private final String firstName;

    private final String email;

    @JsonProperty(value = "phone_number")
    private final String phoneNumber;

    @JsonProperty(value = "social_networks")
    private final List<SocialNetworkLinkDto> socialNetworks;
}
