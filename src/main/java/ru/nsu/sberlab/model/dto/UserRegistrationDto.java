package ru.nsu.sberlab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
    private String email;
    private String phoneNumber;
    private String firstName;
    private String password;
    private boolean hasPermitToShowPhoneNumber;
    private boolean hasPermitToShowEmail;
    private List<SocialNetworkPostDto> socialNetworks;
}