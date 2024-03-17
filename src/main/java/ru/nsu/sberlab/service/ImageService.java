package ru.nsu.sberlab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.sberlab.exception.ImageNotFoundException;
import ru.nsu.sberlab.model.dto.PetImageDataDto;
import ru.nsu.sberlab.model.entity.PetImage;
import ru.nsu.sberlab.dao.PetImageRepository;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final PetImageRepository petImageRepository;

    public PetImageDataDto getPetImageByUUID(String uuidName) {
        PetImage petImage = petImageRepository.findByImageUUIDName(uuidName)
                .orElseThrow(ImageNotFoundException::new);
        return new PetImageDataDto(petImage.getSize(), petImage.getContentType(), petImage.getImageData());
    }
}
