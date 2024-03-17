package ru.nsu.sberlab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UserInfoDto {
    private final String firstName;
    private final String email;
    private final String phoneNumber;
    private final boolean hasPermitToShowEmail;
    private final boolean hasPermitToShowPhoneNumber;
    private final List<SocialNetworkInfoDto> socialNetworks;
}
