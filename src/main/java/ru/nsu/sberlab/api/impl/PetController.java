package ru.nsu.sberlab.api.impl;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.sberlab.api.PetControllerApi;
import ru.nsu.sberlab.model.dto.pet.PetEditDto;
import ru.nsu.sberlab.model.dto.pet.PetInfoDto;
import ru.nsu.sberlab.model.entity.User;
import ru.nsu.sberlab.service.PetService;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/pet/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PetController implements PetControllerApi {
    private final PetService petService;

    // TODO: add captcha to frontend
    @GetMapping(value = "find/{searchParameter}")
    public ResponseEntity<PetInfoDto> getPetInfo(
            @PathVariable(value = "searchParameter") @NotBlank String searchParameter
    ) {
        PetInfoDto petInfo = petService.getPetInfoBySearchParameter(searchParameter);
        return ResponseEntity.status(HttpStatus.OK)
                .body(petInfo);
    }

    @GetMapping(value = "{petId}")
    public ResponseEntity<PetInfoDto> getPetInfo(
            @PathVariable(value = "petId") @NotNull @Min(0) long petId
    ) {
        PetInfoDto petInfo = petService.getPetInfoByPetId(petId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(petInfo);
    }

    @PutMapping(value = "{petId}")
    public ResponseEntity<PetInfoDto> editPet(
            @PathVariable("petId") @Min(value = 0, message = "Pet id should be positive value") long petId,
            @RequestPart("pet") @Validated PetEditDto petEditDto,
            @RequestPart("image_file") MultipartFile imageFile,
            @AuthenticationPrincipal User principal
    ) {
        PetInfoDto updatedPet = petService.updatePet(petId, petEditDto, imageFile, principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedPet);
    }

    @DeleteMapping(value = "{petId}")
    public ResponseEntity<?> deletePet(
            @PathVariable(value = "petId") @Min(value = 0, message = "Pet id should be positive value") long petId,
            @AuthenticationPrincipal User principal
    ) {
        petService.deletePet(petId, principal);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @GetMapping(value = "privileged/list")
    public ResponseEntity<List<PetInfoDto>> privilegedPetsList(
            @PageableDefault Pageable pageable
    ) {
        List<PetInfoDto> pets = petService.petsList(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(pets);
    }
}
