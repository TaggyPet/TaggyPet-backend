package ru.nsu.sberlab.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.sberlab.model.dto.pet.PetEditDto;
import ru.nsu.sberlab.model.dto.pet.PetInfoDto;
import ru.nsu.sberlab.model.entity.User;

import java.util.List;

@Tag(name = "pet", description = "API для работы с pet")
@RequestMapping(value = "api/v1/pet/", produces = MediaType.APPLICATION_JSON_VALUE)
public interface PetControllerApi {
    @Operation(
            summary = "Получить основную информацию о питомце",
            description = "Поиск осуществляется по чипу или клейму питомца. Запрос должен обрабатываться captcha-ей"
    )
    @GetMapping(value = "find/{searchParameter}")
    ResponseEntity<PetInfoDto> getPetInfo(
            @PathVariable(value = "searchParameter") @NotBlank String searchParameter
    );

    @Operation(
            summary = "Получить основную информацию о питомце",
            description = "Поиск осуществляется по id питомца"
    )
    @GetMapping(value = "{petId}")
    ResponseEntity<PetInfoDto> getPetInfo(
            @PathVariable(value = "petId") @NotNull @Min(0) long petId
    );

    @Operation(summary = "Редактирование питомца")
    @PutMapping(value = "{petId}")
    ResponseEntity<PetInfoDto> editPet(
            @PathVariable("petId") @Min(value = 0, message = "Pet id should be positive value") long petId,
            @RequestPart("pet") @Validated PetEditDto petEditDto,
            @RequestPart("image_file") MultipartFile imageFile,
            @AuthenticationPrincipal User principal
    );

    @Operation(summary = "Удаление питомца")
    @DeleteMapping(value = "{petId}")
    ResponseEntity<?> deletePet(
            @PathVariable(value = "petId") @Min(value = 0, message = "Pet id should be positive value") long petId,
            @AuthenticationPrincipal User principal
    );

    @Operation(summary = "Получить привилегированный список всех питомцев")
    @GetMapping(value = "privileged/list")
    ResponseEntity<List<PetInfoDto>> privilegedPetsList(
            @PageableDefault Pageable pageable
    );
}
