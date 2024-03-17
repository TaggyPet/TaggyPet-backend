package ru.nsu.sberlab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UserCardDto {
    private final String firstName;
    private final String email;
    private final String phoneNumber;
    private final List<SocialNetworkLinkDto> socialNetworks;
}
