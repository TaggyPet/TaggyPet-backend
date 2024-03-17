package ru.nsu.sberlab.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.sberlab.model.dto.DeletedPetDto;
import ru.nsu.sberlab.model.dto.PetEditDto;
import ru.nsu.sberlab.model.dto.PetInfoDto;
import ru.nsu.sberlab.model.entity.User;
import ru.nsu.sberlab.service.PetService;
import lombok.RequiredArgsConstructor;
import ru.nsu.sberlab.service.ReCaptchaService;

import java.util.List;

@RestController
@RequestMapping(value = "/pet/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;
//    private final ReCaptchaService reCaptchaService; // FIXME

    // TODO: add captcha to frontend
    @GetMapping(value = "find/{searchParameter}")
    public ResponseEntity<PetInfoDto> getPetInfo(
//            @RequestParam(name = "g-recaptcha-response") String response,
            @PathVariable(value = "searchParameter") @NotBlank String searchParameter
    ) {
//        reCaptchaService.verify(response, "find", "/");
        PetInfoDto petInfo = petService.getPetInfoBySearchParameter(searchParameter);
        return ResponseEntity.status(HttpStatus.OK)
                .body(petInfo);
    }

    @PatchMapping(value = "{petId}")
    public ResponseEntity<PetInfoDto> editPet(
            @PathVariable("petId") @Min(value = 0, message = "Pet id should be positive value") long petId,
            @RequestPart("pet") PetEditDto petEditDto,
            @RequestPart("imageFile") MultipartFile imageFile,
            @AuthenticationPrincipal User principal
    ) {
        PetInfoDto updatedPet = petService.updatePet(petId, petEditDto, imageFile, principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedPet);
    }

    @DeleteMapping(value = "{petId}")
    public ResponseEntity<DeletedPetDto> deletePet(
            @PathVariable(value = "petId") @Min(value = 0, message = "Pet id should be positive value") long id,
            @AuthenticationPrincipal User principal
    ) {
        DeletedPetDto deletedPet = petService.deletePet(id, principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(deletedPet);
    }

    @GetMapping(value = "privileged-list")
    public ResponseEntity<List<PetInfoDto>> privilegedPetsList(
            @PageableDefault Pageable pageable
    ) {
        List<PetInfoDto> pets = petService.petsList(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(pets);
    }
}
