package ru.nsu.sberlab.api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import ru.nsu.sberlab.api.UserControllerApi;
import ru.nsu.sberlab.model.dto.pet.PetCreationDto;
import ru.nsu.sberlab.model.dto.pet.PetInfoDto;
import ru.nsu.sberlab.model.dto.user.PersonalCabinetDto;
import ru.nsu.sberlab.model.dto.user.UserEditDto;
import ru.nsu.sberlab.model.dto.user.UserInfoDto;
import ru.nsu.sberlab.model.dto.user.UserRegistrationDto;
import ru.nsu.sberlab.model.entity.User;
import ru.nsu.sberlab.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController implements UserControllerApi {
    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<?> getUserPersonalCabinetInfo(@AuthenticationPrincipal User user) {
        PersonalCabinetDto personalCabinetDto = userService.getPersonalCabinetDtoByEmail(user.getEmail());
        return ResponseEntity.ok(personalCabinetDto);
    }

    @GetMapping("/edit")
    public ResponseEntity<?> getUserEditInfo(@AuthenticationPrincipal User user) {
        UserInfoDto userInfoDto = userService.getUserInfoDtoByEmail(user.getEmail());
        return ResponseEntity.ok(userInfoDto);
    }

    @GetMapping("/pets")
    public ResponseEntity<List<PetInfoDto>> listOfPets(@AuthenticationPrincipal User principal) {
        List<PetInfoDto> pets = userService.petsListByUserId(principal.getUserId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(pets);
    }

    @PostMapping("/pets")
    public ResponseEntity<?> createPet(
            @RequestPart("pet") @Validated PetCreationDto pet,
            @RequestPart("image_file") MultipartFile imageFile,
            @AuthenticationPrincipal User principal, UriComponentsBuilder uriComponentsBuilder
    ) {
        String searchParameter = userService.createPet(pet, imageFile, principal);
        return ResponseEntity.created(uriComponentsBuilder
                        .path("/pet/find/{searchParameter}")
                        .build(Map.of("searchParameter", searchParameter)))
                .build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal User principal) {
        userService.deleteUser(principal.getUserId());
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editProfile(
            @RequestBody @Validated UserEditDto editedUser,
            @AuthenticationPrincipal User principal
    ) {
        userService.updateUserInfo(editedUser, principal);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
