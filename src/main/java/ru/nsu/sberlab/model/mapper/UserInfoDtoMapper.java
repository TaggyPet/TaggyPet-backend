package ru.nsu.sberlab.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.sberlab.model.dto.UserInfoDto;
import ru.nsu.sberlab.model.entity.User;
import ru.nsu.sberlab.service.UserSocialNetworkService;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserInfoDtoMapper implements Function<User, UserInfoDto> {
    private final UserSocialNetworkService userSocialNetworkService;

    @Override
    public UserInfoDto apply(User user) {
        return new UserInfoDto(
                user.getFirstName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.isHasPermitToShowEmail(),
                user.isHasPermitToShowPhoneNumber(),
                userSocialNetworkService.getAllSocialNetworksInfoDtoByUser(user)
        );
    }
}
