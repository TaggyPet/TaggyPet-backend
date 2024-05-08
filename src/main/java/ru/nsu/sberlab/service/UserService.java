package ru.nsu.sberlab.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.sberlab.dao.DeletedUserRepository;
import ru.nsu.sberlab.dao.PetRepository;
import ru.nsu.sberlab.dao.UserRepository;
import ru.nsu.sberlab.exception.*;
import ru.nsu.sberlab.model.dto.pet.PetCreationDto;
import ru.nsu.sberlab.model.dto.pet.PetInfoDto;
import ru.nsu.sberlab.model.dto.user.PersonalCabinetDto;
import ru.nsu.sberlab.model.dto.user.UserEditDto;
import ru.nsu.sberlab.model.dto.user.UserInfoDto;
import ru.nsu.sberlab.model.dto.user.UserRegistrationDto;
import ru.nsu.sberlab.model.entity.DeletedUser;
import ru.nsu.sberlab.model.entity.Pet;
import ru.nsu.sberlab.model.entity.PetImage;
import ru.nsu.sberlab.model.entity.User;
import ru.nsu.sberlab.model.mapper.PersonalCabinetDtoMapper;
import ru.nsu.sberlab.model.mapper.PetInfoDtoMapper;
import ru.nsu.sberlab.model.mapper.UserInfoDtoMapper;
import ru.nsu.sberlab.model.util.FeaturesConverter;
import ru.nsu.sberlab.model.util.PetCleaner;
import ru.nsu.sberlab.model.util.SocialNetworksConverter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
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

    @Value(value = "${default.pet.image.name}")
    private String defaultPetImageName;

    @Value(value = "${default.not.specified.phone.number}")
    private String defaultNotSpecifiedPhoneNumber;

    @Transactional
    public void createUser(UserRegistrationDto userDto) {
        Optional<User> currentUser = userRepository.findByEmail(userDto.getEmail());
        if (currentUser.isPresent() && currentUser.get().isActive()) {
            throw new FailedUserCreationException("api.server.error.user-not-created");
        }
        log.info("Received request to create user with payload: {}", userDto);
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
    }

    @Transactional
    public void updateUserInfo(UserEditDto userEditDto, User principle) {
        log.info("Received request to update from user with email: {}", userEditDto.getEmail());
        User user = userRepository.findByEmail(userEditDto.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("api.server.error.user-not-found")
        );
        if (!principle.getEmail().equals(userEditDto.getEmail())) {
            throw new IllegalAccessToUserException("api.server.error.does-not-have-access-to-user");
        }
        if (userEditDto.getPreviousPassword().equals(userEditDto.getNewPassword())) {
            throw new PreviousPasswordMatchesWithNewPasswordException("api.server.error.new-password-matches-with-previous-password");
        }
        if (Objects.nonNull(userEditDto.getNewPassword())) {
            user.setPassword(passwordEncoder.encode(userEditDto.getNewPassword()));
        }
        user.setFirstName(userEditDto.getFirstName());
        user.setPhoneNumber(userEditDto.getPhoneNumber().isBlank() ? defaultNotSpecifiedPhoneNumber : userEditDto.getPhoneNumber());
        user.setHasPermitToShowEmail(userEditDto.isHasPermitToShowEmail());
        user.setHasPermitToShowPhoneNumber(userEditDto.isHasPermitToShowPhoneNumber());

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
        log.info("Received request to deletion from user with email: {}", user.getEmail());
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

    /**
     *
     * @param petCreationDto
     * @param imageFile
     * @param principal
     * @return search parameter for created pet
     */
    @Transactional
    public String createPet(PetCreationDto petCreationDto, MultipartFile imageFile, User principal) {
        User user = userRepository.findUserByUserId(principal.getUserId()).orElseThrow(
                () -> new UsernameNotFoundException("api.server.error.user-not-found")
        );
        log.info("Received request from user with email: {} to create a new pet", principal.getEmail());

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
        return Objects.nonNull(createdPet.getChipId()) ? createdPet.getChipId() : createdPet.getStampId();
    }

    public List<PetInfoDto> petsListByUserId(Long userId) {
        log.info("Received request to pet list from user with id: {}", userId);
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
                .orElseThrow(() -> new UsernameNotFoundException("api.server.error.user-not-found"));
    }

    public PersonalCabinetDto getPersonalCabinetDtoByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(personalCabinetDtoMapper)
                .orElseThrow(() -> new UsernameNotFoundException("api.server.error.user-not-found"));
    }

    @Override
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("api.server.error.user-not-found")
        );
    }
}
