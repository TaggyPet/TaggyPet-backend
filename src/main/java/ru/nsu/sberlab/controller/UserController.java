package ru.nsu.sberlab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import ru.nsu.sberlab.model.dto.*;
import ru.nsu.sberlab.model.entity.User;
import ru.nsu.sberlab.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value = "registration")
    public ResponseEntity<?> createUser(@RequestBody @Validated UserRegistrationDto user) {
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @GetMapping(value = "pets")
    public ResponseEntity<List<PetInfoDto>> listOfPets(
            @AuthenticationPrincipal User principal
    ) {
        List<PetInfoDto> pets = userService.petsListByUserId(principal.getUserId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(pets);
    }

    @PostMapping(value = "pets")
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
    public ResponseEntity<String> deleteAccount(@AuthenticationPrincipal User principal) {
        userService.deleteUser(principal.getUserId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(principal.getEmail());
    }

    @PutMapping
    public ResponseEntity<?> editProfile(
            @RequestBody @Validated UserEditDto editedUser,
            @AuthenticationPrincipal User principal
    ) {
        userService.updateUserInfo(editedUser, principal);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
