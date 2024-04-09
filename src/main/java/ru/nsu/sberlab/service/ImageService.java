package ru.nsu.sberlab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nsu.sberlab.dao.PetImageRepository;
import ru.nsu.sberlab.exception.ImageNotFoundException;
import ru.nsu.sberlab.model.dto.PetImageDataDto;
import ru.nsu.sberlab.model.entity.PetImage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final PetImageRepository petImageRepository;

    @Value(value = "${default.pet.image.name}")
    private String defaultPetImageName;

    public PetImageDataDto getPetImageByUUID(String uuidName) {
        if (defaultPetImageName.equals(uuidName)) {
            ClassLoader imageServiceClassLoader = this.getClass().getClassLoader();
            URL defaultImageURL = imageServiceClassLoader.getResource("static/images/" + defaultPetImageName);
            try (FileInputStream fileInputStream = new FileInputStream(defaultImageURL.getFile())) {
                byte[] imageBytes = fileInputStream.readAllBytes();
                return new PetImageDataDto((long) imageBytes.length, "image/png", imageBytes);
            } catch (IOException exception) {
                throw new UncheckedIOException(exception);
            }
        }
        PetImage petImage = petImageRepository.findTopByImageUUIDName(uuidName)
                .orElseThrow(ImageNotFoundException::new);
        return new PetImageDataDto(petImage.getSize(), petImage.getContentType(), petImage.getImageData());
    }
}
