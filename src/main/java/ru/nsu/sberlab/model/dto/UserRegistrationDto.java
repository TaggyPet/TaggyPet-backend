package ru.nsu.sberlab.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegistrationDto {
    @NotBlank
    @Email
    private String email;

    @NotNull
    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @NotBlank
    @JsonProperty(value = "first_name")
    private String firstName;

    @NotBlank
    @Length(min=6, max=50)
    private String password;

    @JsonProperty(value = "has_permit_to_show_phone_number")
    private boolean hasPermitToShowPhoneNumber;

    @JsonProperty(value = "has_permit_to_show_email")
    private boolean hasPermitToShowEmail;

    @JsonProperty(value = "social_networks")
    @Valid
    private List<SocialNetworkPostDto> socialNetworks;

    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", hasPermitToShowPhoneNumber=" + hasPermitToShowPhoneNumber +
                ", hasPermitToShowEmail=" + hasPermitToShowEmail +
                '}';
    }
}
