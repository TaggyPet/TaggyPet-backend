package ru.nsu.sberlab.model.mapper;

import org.springframework.stereotype.Service;
import ru.nsu.sberlab.model.dto.SocialNetworkLinkDto;
import ru.nsu.sberlab.model.dto.UserCardDto;
import ru.nsu.sberlab.model.entity.User;

import java.util.function.Function;

@Service
public class UserCardDtoMapper implements Function<User, UserCardDto> {
    @Override
    public UserCardDto apply(User user) {
        String handledEmail = user.isHasPermitToShowEmail() ? user.getEmail() : null;
        String handledPhoneNumber = user.isHasPermitToShowPhoneNumber() ? user.getPhoneNumber() : null;
        return new UserCardDto(
                user.getFirstName(),
                handledEmail,
                handledPhoneNumber,
                user.getUserSocialNetworks()
                        .stream()
                        .map(socialNetwork -> new SocialNetworkLinkDto(
                                        socialNetwork.getSocialNetwork().getPropertyValue(),
                                        socialNetwork.getSocialNetwork().getBaseUrl(),
                                        socialNetwork.getShortName()
                                )
                        )
                        .toList()
        );
    }
}
