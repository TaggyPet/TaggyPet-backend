package ru.nsu.sberlab.model.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletedPetDto {
    private String chipId;
    private String stampId;
}
