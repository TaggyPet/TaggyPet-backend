package ru.nsu.sberlab.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import ru.nsu.sberlab.model.dto.pet.PetCreationDto;
import ru.nsu.sberlab.model.dto.pet.PetInfoDto;
import ru.nsu.sberlab.model.dto.user.UserEditDto;
import ru.nsu.sberlab.model.dto.user.UserRegistrationDto;
import ru.nsu.sberlab.model.entity.User;

import java.util.List;

@Tag(name = "user", description = "API для работы с user")
@RequestMapping(value = "api/v1/user/", produces = MediaType.APPLICATION_JSON_VALUE)
public interface UserControllerApi {
    @Operation(summary = "Регистрация пользователя")
    @PostMapping(value = "registration")
    ResponseEntity<?> createUser(@RequestBody @Validated UserRegistrationDto user);

    @Operation(summary = "Получения списка питомцев пользователя")
    @GetMapping(value = "pets")
    ResponseEntity<List<PetInfoDto>> listOfPets(@AuthenticationPrincipal User principal);

    @Operation(summary = "Добавление питомца")
    @PostMapping(value = "pets")
    ResponseEntity<?> createPet(
            @RequestPart("pet") @Validated PetCreationDto pet,
            @RequestPart("image_file") MultipartFile imageFile,
            @AuthenticationPrincipal User principal, UriComponentsBuilder uriComponentsBuilder
    );

    @Operation(summary = "Удаление аккаунта пользователя")
    @DeleteMapping
    ResponseEntity<?> deleteAccount(@AuthenticationPrincipal User principal);

    @Operation(summary = "Изменение профиля пользователя")
    @PutMapping
    ResponseEntity<?> editProfile(
            @RequestBody @Validated UserEditDto editedUser,
            @AuthenticationPrincipal User principal
    );
}
