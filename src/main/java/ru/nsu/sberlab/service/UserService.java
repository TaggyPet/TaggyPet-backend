package ru.nsu.sberlab.service;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.sberlab.dao.PetRepository;
import ru.nsu.sberlab.exception.FailedPetCreationException;
import ru.nsu.sberlab.exception.FailedUserCreationException;
import ru.nsu.sberlab.exception.AddPetImageException;
import ru.nsu.sberlab.exception.IllegalAccessToUserException;
import ru.nsu.sberlab.model.dto.*;
import ru.nsu.sberlab.model.entity.*;
import ru.nsu.sberlab.model.mapper.PersonalCabinetDtoMapper;
import ru.nsu.sberlab.model.mapper.PetInfoDtoMapper;
import ru.nsu.sberlab.model.mapper.UserInfoDtoMapper;
import ru.nsu.sberlab.model.util.FeaturesConverter;
import ru.nsu.sberlab.model.util.PetCleaner;
import ru.nsu.sberlab.model.util.SocialNetworksConverter;
import ru.nsu.sberlab.dao.DeletedUserRepository;
import ru.nsu.sberlab.dao.UserRepository;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final DeletedUserRepository deletedUserRepository;
    private final PetRepository petRepository;
    private final PasswordEncoder passwordEncoder;
    private final SocialNetworksConverter socialNetworksConverter;
    private final FeaturesConverter featuresConverter;
    private final PetInfoDtoMapper petInfoDtoMapper;
    private final UserInfoDtoMapper userInfoDtoMapper;
    private final PersonalCabinetDtoMapper personalCabinetDtoMapper;
    private final PetCleaner petCleaner;
    private final PropertyResolverUtils propertyResolver;

    @Value("${default.pet.image.name}")
    private String defaultPetImageName;

    @Transactional
    public UserInfoDto createUser(UserRegistrationDto userDto) {
        Optional<User> currentUser = userRepository.findByEmail(userDto.getEmail());
        if (currentUser.isPresent() && currentUser.get().isActive()) {
            throw new FailedUserCreationException("api.server.error.user-not-created");
        }
        User user = new User(
                userDto.getEmail(),
                userDto.getPhoneNumber(),
                userDto.getFirstName(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.isHasPermitToShowPhoneNumber(),
                userDto.isHasPermitToShowEmail()
        );
        User savedUser = userRepository.save(user);
        savedUser.setUserSocialNetworks(socialNetworksConverter.convertSocialNetworksDtoToSocialNetworks(
                        userDto.getSocialNetworks(),
                        savedUser
                )
        );
        return userInfoDtoMapper.apply(savedUser);
    }

    @Transactional
    public void updateUserInfo(UserEditDto userEditDto, User principle) {
        User user = userRepository.findByEmail(userEditDto.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("api.server.error.user-not-found")
        );
        if (!principle.getEmail().equals(userEditDto.getEmail())) {
            throw new IllegalAccessToUserException("api.server.error.does-not-have-access-to-user");
        }
        if (Objects.nonNull(userEditDto.getFirstName())) {
            user.setFirstName(userEditDto.getFirstName());
        }
        if (Objects.nonNull(userEditDto.getPhoneNumber())) {
            user.setPhoneNumber(userEditDto.getPhoneNumber());
        }
        if (Objects.nonNull(userEditDto.getPassword()) && !userEditDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userEditDto.getPassword()));
        }
        if (Objects.nonNull(userEditDto.isHasPermitToShowEmail())) {
            user.setHasPermitToShowEmail(userEditDto.isHasPermitToShowEmail());
        }
        if (Objects.nonNull(userEditDto.isHasPermitToShowPhoneNumber())) {
            user.setHasPermitToShowPhoneNumber(userEditDto.isHasPermitToShowPhoneNumber());
        }

        User updatedUser = userRepository.save(user);
        if (Objects.nonNull(userEditDto.getSocialNetworks())) {
            updatedUser.getUserSocialNetworks().clear();
            updatedUser.getUserSocialNetworks().addAll(socialNetworksConverter.convertSocialNetworksDtoToSocialNetworks(
                    userEditDto.getSocialNetworks(),
                    updatedUser
            ));
        }
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findUserByUserId(userId).orElseThrow(
                () -> new UsernameNotFoundException("api.server.error.user-not-found")
        );
        DeletedUser deletedUser = new DeletedUser(
                user.getUserId(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getFirstName(),
                user.getDateOfCreated()
        );
        deletedUserRepository.save(deletedUser);

        user.getUserSocialNetworks().clear();
        user.getFeatures().clear();
        List<Pet> pets = user.getPets();
        pets.forEach(pet -> petCleaner.detachUser(pet, user));
        pets.forEach(petCleaner::detachFeatures);
        userRepository.deleteByUserId(user.getUserId());
        pets.forEach(petCleaner::removePet);
    }

    @Transactional
    public PetInfoDto createPet(PetCreationDto petCreationDto, MultipartFile imageFile, User principal) {
        User user = userRepository.findUserByUserId(principal.getUserId()).orElseThrow(
                () -> new UsernameNotFoundException("api.server.error.user-not-found")
        );
        PetImage petImage = new PetImage();
        if (Objects.isNull(imageFile) || imageFile.isEmpty()) {
            petImage.setImageUUIDName(defaultPetImageName);
        } else {
            try {
                petImage.setImageData(imageFile.getBytes());
                petImage.setImageUUIDName(UUID.randomUUID() + imageFile.getName());
                petImage.setContentType(imageFile.getContentType());
                petImage.setSize(imageFile.getSize());
            } catch (IOException exception) {
                throw new AddPetImageException(exception.getMessage());
            }
        }
        if (petRepository.findByChipId(petCreationDto.getChipId()).isPresent() ||
            petRepository.findByStampId(petCreationDto.getStampId()).isPresent()) {
            throw new FailedPetCreationException("api.server.error.pet-not-created");
        }
        Pet createdPet = new Pet(
                petCreationDto.getChipId(),
                petCreationDto.getStampId(),
                petCreationDto.getType(),
                petCreationDto.getBreed(),
                petCreationDto.getSex(),
                petCreationDto.getName(),
                featuresConverter.convertFeatureDtoListToFeatures(petCreationDto.getFeatures(), user),
                petImage
        );
        user.getPets().add(createdPet);
        userRepository.save(user);
        return petInfoDtoMapper.apply(createdPet);
    }

    public List<PetInfoDto> petsListByUserId(Long userId) {
        return userRepository.findUserByUserId(userId).orElseThrow(
                        () -> new UsernameNotFoundException("api.server.error.user-not-found")
                )
                .getPets()
                .stream()
                .map(petInfoDtoMapper)
                .toList();
    }

    public UserInfoDto getUserInfoDtoByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userInfoDtoMapper)
                .orElseThrow(() -> new UsernameNotFoundException(message("api.server.error.user-not-found")));
    }

    public PersonalCabinetDto getPersonalCabinetDtoByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(personalCabinetDtoMapper)
                .orElseThrow(() -> new UsernameNotFoundException(message("api.server.error.user-not-found")));
    }

    @Override
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("api.server.error.user-not-found")
        );
    }

    private String message(String property) { // TODO: maybe move this method to ErrorController
        return propertyResolver.resolve(property, Locale.getDefault());
    }
}
