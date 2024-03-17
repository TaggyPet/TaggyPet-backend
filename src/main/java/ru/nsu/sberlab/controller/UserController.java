package ru.nsu.sberlab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.sberlab.model.dto.*;
import ru.nsu.sberlab.model.entity.User;
import ru.nsu.sberlab.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/user/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value = "registration")
    public ResponseEntity<UserInfoDto> createUser(@RequestBody UserRegistrationDto user) {
        UserInfoDto createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(createdUser);
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
    public ResponseEntity<PetInfoDto> createPet(
            @RequestPart("pet") PetCreationDto pet,
            @RequestPart("imageFile") MultipartFile imageFile,
            @AuthenticationPrincipal User principal
    ) {
        PetInfoDto createdPet = userService.createPet(pet, imageFile, principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(createdPet);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAccount(@AuthenticationPrincipal User principal) {
        userService.deleteUser(principal.getUserId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(principal.getEmail());
    }

    @PatchMapping
    public ResponseEntity<?> editProfile(
            @RequestBody UserEditDto editedUser,
            @AuthenticationPrincipal User principal
    ) {
        userService.updateUserInfo(editedUser, principal);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
