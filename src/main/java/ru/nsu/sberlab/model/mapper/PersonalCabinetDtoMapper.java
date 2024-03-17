package ru.nsu.sberlab.model.mapper;

import org.springframework.stereotype.Service;
import ru.nsu.sberlab.model.dto.*;
import ru.nsu.sberlab.model.entity.User;

import java.util.function.Function;

@Service
public class PersonalCabinetDtoMapper implements Function<User, PersonalCabinetDto> {
    @Override
    public PersonalCabinetDto apply(User user) {
        return new PersonalCabinetDto(
                user.getFirstName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getUserSocialNetworks()
                        .stream()
                        .map(userSocialNetwork -> new SocialNetworkOverviewDto(
                                userSocialNetwork.getSocialNetwork().getPropertyValue(),
                                userSocialNetwork.getShortName()
                        ))
                        .toList(),
                user.getPets()
                        .stream()
                        .map(pet -> new PetCardDto(
                                pet.getChipId(),
                                pet.getStampId(),
                                pet.getName(),
                                new PetImageDto(pet.getPetImage().getImageUUIDName())
                        ))
                        .toList()
        );
    }
}
