package ru.nsu.sberlab.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SocialNetworkOverviewDto {
    private final String name;
    private final String login;
}
