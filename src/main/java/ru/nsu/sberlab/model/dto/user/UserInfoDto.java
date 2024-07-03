package ru.nsu.sberlab.model.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.nsu.sberlab.model.dto.socialnetwork.SocialNetworkInfoDto;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class UserInfoDto { // FIXME: this dto maybe useless
    @JsonProperty(value = "first_name")
    private final String firstName;

    private final String email;

    @JsonProperty(value = "phone_number")
    private final String phoneNumber;

    @JsonProperty(value = "has_permit_to_show_email")
    private final boolean hasPermitToShowEmail;

    @JsonProperty(value = "has_permit_to_show_phone_number")
    private final boolean hasPermitToShowPhoneNumber;

    @JsonProperty(value = "social_networks")
    private final List<SocialNetworkInfoDto> socialNetworks;
}
