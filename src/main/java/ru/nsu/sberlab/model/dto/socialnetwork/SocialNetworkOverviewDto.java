package ru.nsu.sberlab.model.dto.socialnetwork;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SocialNetworkOverviewDto {
    private final String name;
    private final String login;
}
