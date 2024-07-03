package ru.nsu.sberlab.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "image", description = "API для работы с изображениями")
public interface ImageControllerApi {
    @Operation(
            summary = "Получение изображения питомца по UUID изображения",
            description = "Если у питомца нет изображения (UUID равен default-pet-image.png), то вернется картинка по умолчанию"
    )
    @GetMapping("api/v1/pet/images/{uuidName}")
    ResponseEntity<InputStreamResource> getPetImageByUUIDName(@PathVariable String uuidName);
}
