package ru.nsu.sberlab.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
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
public class UserEditDto {
    @NotBlank
    @Email
    private String email;

    @JsonProperty(value = "first_name")
    @NotBlank
    private String firstName;

    @JsonProperty(value = "phone_number")
    @NotNull
    private String phoneNumber;

    @JsonProperty(value = "has_permit_to_show_phone_number")
    private boolean hasPermitToShowPhoneNumber;

    @JsonProperty(value = "has_permit_to_show_email")
    private boolean hasPermitToShowEmail;

    @JsonProperty(value = "previous_password")
    @NotBlank
    @Length(min=6, max=50)
    private String previousPassword;

    @JsonProperty(value = "new_password")
    @Length(min=6, max=50)
    @Nullable
    private String newPassword;

    @JsonProperty(value = "social_networks")
    @Valid
    private List<SocialNetworkPostDto> socialNetworks;

    @Override
    public String toString() {
        return "UserEditDto{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", hasPermitToShowPhoneNumber=" + hasPermitToShowPhoneNumber +
                ", hasPermitToShowEmail=" + hasPermitToShowEmail +
                '}';
    }
}
