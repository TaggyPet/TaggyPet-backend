package ru.nsu.sberlab.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PetCardDto {
    private final String chipId;
    private final String stampId;
    private final String name;
    private final PetImageDto petImageDto;
}
