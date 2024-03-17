package ru.nsu.sberlab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.sberlab.model.dto.PetImageDataDto;
import ru.nsu.sberlab.service.ImageService;

import java.io.ByteArrayInputStream;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/pet/images/{uuidName}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getPetImageByUUIDName(@PathVariable String uuidName) {
        PetImageDataDto petImage = imageService.getPetImageByUUID(uuidName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(petImage.getContentType()))
                .contentLength(petImage.getImageSize())
                .body(new InputStreamResource(new ByteArrayInputStream(petImage.getImageData())));
    }
}
