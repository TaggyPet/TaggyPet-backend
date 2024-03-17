package ru.nsu.sberlab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.sberlab.model.entity.SocialNetwork;
import ru.nsu.sberlab.dao.SocialNetworkPropertiesRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialNetworkPropertiesService {
    private final SocialNetworkPropertiesRepository socialNetworkPropertiesRepository;

    public List<SocialNetwork> properties() {
        return socialNetworkPropertiesRepository.findAll();
    }
}
