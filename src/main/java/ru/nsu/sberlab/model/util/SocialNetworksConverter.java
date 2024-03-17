package ru.nsu.sberlab.model.util;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.stereotype.Service;
import ru.nsu.sberlab.exception.PropertyNotFoundException;
import ru.nsu.sberlab.model.dto.SocialNetworkPostDto;
import ru.nsu.sberlab.model.entity.UserSocialNetwork;
import ru.nsu.sberlab.model.entity.User;
import ru.nsu.sberlab.dao.SocialNetworkPropertiesRepository;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SocialNetworksConverter {
    private final SocialNetworkPropertiesRepository socialNetworkPropertiesRepository;
    private final PropertyResolverUtils propertyResolver;

    public List<UserSocialNetwork> convertSocialNetworksDtoToSocialNetworks(
            List<SocialNetworkPostDto> socialNetworkRegistrationDtoList,
            User user
    ) {
        return socialNetworkRegistrationDtoList
                .stream()
                .filter(dto -> !dto.getShortName().isEmpty())
                .map(dto -> new UserSocialNetwork(
                                dto.getShortName(),
                                socialNetworkPropertiesRepository.findById(dto.getPropertyId()).orElseThrow(
                                        () -> new PropertyNotFoundException(
                                                message("api.server.error.property-not-found")
                                        )
                                ),
                                user
                        )
                )
                .toList();
    }

    private String message(String property) {
        return propertyResolver.resolve(property, Locale.getDefault());
    }
}
