package ru.nsu.sberlab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PetImageDataDto {
    private final Long imageSize;
    private final String contentType;
    private final byte[] imageData;
}
