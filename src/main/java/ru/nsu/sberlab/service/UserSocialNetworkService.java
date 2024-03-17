package ru.nsu.sberlab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nsu.sberlab.dao.SocialNetworkPropertiesRepository;
import ru.nsu.sberlab.model.dto.SocialNetworkInfoDto;
import ru.nsu.sberlab.model.entity.User;
import ru.nsu.sberlab.model.entity.UserSocialNetwork;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSocialNetworkService {
    private final SocialNetworkPropertiesRepository socialNetworkPropertiesRepository;

    @Value("${social.network.empty.login}")
    private String emptyLogin;

    public List<SocialNetworkInfoDto> getAllSocialNetworksInfoDtoByUser(User user) {
        return socialNetworkPropertiesRepository.findAll()
                .stream()
                .map(socialNetwork -> new SocialNetworkInfoDto(
                        socialNetwork.getPropertyId(),
                        socialNetwork.getPropertyValue(),
                        getLoginByPropertyId(user, socialNetwork.getPropertyId())))
                .toList();
    }

    private String getLoginByPropertyId(User user, Long propertyId) {
        for (UserSocialNetwork socialNetwork : user.getUserSocialNetworks()) {
            if (socialNetwork.getSocialNetwork().getPropertyId().equals(propertyId)) {
                return socialNetwork.getShortName();
            }
        }
        return emptyLogin;
    }
}
