package ru.nsu.sberlab.service;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.sberlab.exception.AddPetImageException;
import ru.nsu.sberlab.exception.FailedPetSearchException;
import ru.nsu.sberlab.exception.IllegalAccessToPetException;
import ru.nsu.sberlab.exception.PetNotFoundException;
import ru.nsu.sberlab.model.dto.DeletedPetDto;
import ru.nsu.sberlab.model.dto.PetEditDto;
import ru.nsu.sberlab.model.dto.PetInfoDto;
import ru.nsu.sberlab.model.entity.Feature;
import ru.nsu.sberlab.model.entity.Pet;
import ru.nsu.sberlab.model.entity.PetImage;
import ru.nsu.sberlab.model.entity.User;
import ru.nsu.sberlab.model.mapper.PetEditDtoMapper;
import ru.nsu.sberlab.model.mapper.PetInfoDtoMapper;
import ru.nsu.sberlab.model.util.FeaturesConverter;
import ru.nsu.sberlab.model.util.PetCleaner;
import ru.nsu.sberlab.dao.FeaturePropertiesRepository;
import ru.nsu.sberlab.dao.PetRepository;
import ru.nsu.sberlab.dao.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final FeaturePropertiesRepository featurePropertiesRepository;
    private final PetInfoDtoMapper petInfoDtoMapper;
    private final PetEditDtoMapper petEditDtoMapper;
    private final FeaturesConverter featuresConverter;
    private final PetCleaner petCleaner;
    private final PropertyResolverUtils propertyResolver;

    public PetInfoDto getPetInfoBySearchParameter(String searchParameter) {
        if (isChipIdParameter(searchParameter)) {
            return petRepository.findByChipId(searchParameter)
                    .map(petInfoDtoMapper)
                    .orElseThrow(() -> new FailedPetSearchException(searchParameter));
        } else {
            return petRepository.findByStampId(searchParameter)
                    .map(petInfoDtoMapper)
                    .orElseThrow(() -> new FailedPetSearchException(searchParameter));
        }
    }

    public PetEditDto getPetEditDtoByChipId(String chipId) { // TODO: maybe remove
        return petRepository.findByChipId(chipId)
                .map(pet -> petEditDtoMapper.apply(pet, featurePropertiesRepository.findAll()))
                .orElseThrow(() -> new PetNotFoundException("api.server.error.pet-not-found"));
    }

    /**
     * <p> Update information about user's pet.
     * Note: this method doesn't delete features from features table.
     * </p>
     *
     * @param petEditDto this parameter contains new information about pet
     * @param principal  this parameter present user authentication session
     * @author Kirill Bolotov
     */
    @Transactional
    public PetInfoDto updatePet(long petId, PetEditDto petEditDto, MultipartFile imageFile, User principal) {
        Pet pet = petRepository.findById(petId).orElseThrow(
                () -> new PetNotFoundException("api.server.error.pet-not-found")
        );
        User currentUser = userRepository.findByEmail(principal.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("api.server.error.user-not-found")
        );
        checkIfUserHasAccessToPet(currentUser, pet);

        if (Objects.nonNull(petEditDto.getType())) {
            pet.setType(petEditDto.getType());
        }
        if (Objects.nonNull(petEditDto.getBreed())) {
            pet.setBreed(petEditDto.getBreed());
        }
        if (Objects.nonNull(petEditDto.getSex())) {
            pet.setSex(petEditDto.getSex());
        }
        if (Objects.nonNull(petEditDto.getName())) {
            pet.setName(petEditDto.getName());
        }
        if (Objects.nonNull(petEditDto.getFeatures())) {
            Map<Long, Feature> featureMap = pet.getFeatures()
                    .stream()
                    .collect(Collectors.toMap(
                            feature -> feature.getProperty().getPropertyId(), feature -> feature, (a, b) -> b)
                    );
            List<Feature> mergedFeatures = featuresConverter.convertFeatureDtoListToFeatures(
                            petEditDto.getFeatures(),
                            currentUser
                    )
                    .stream()
                    .map(feature -> {
                                Long key = feature.getProperty().getPropertyId();
                                Feature value = featureMap.get(key);
                                if (Objects.nonNull(value)) {
                                    value.setDescription(feature.getDescription());
                                    value.setDateTime(LocalDate.now());
                                    return value;
                                }
                                return feature;
                            }
                    )
                    .collect(Collectors.toCollection(ArrayList<Feature>::new));
            pet.setFeatures(mergedFeatures);
        }
        if (Objects.nonNull(imageFile) && !imageFile.isEmpty()) { // FIXME: delete unused photos
            PetImage petImage = new PetImage();
            try {
                petImage.setImageData(imageFile.getBytes());
                petImage.setImageUUIDName(UUID.randomUUID() + imageFile.getName());
                petImage.setContentType(imageFile.getContentType());
                petImage.setSize(imageFile.getSize());
            } catch (IOException exception) {
                throw new AddPetImageException(exception.getMessage());
            }
            pet.setPetImage(petImage);
        }
        Pet savedPet = petRepository.save(pet);
        return petInfoDtoMapper.apply(savedPet);
    }

    @Transactional
    public DeletedPetDto deletePet(long petId, User principal) {
        Pet pet = petRepository.findById(petId).orElseThrow(
                () -> new PetNotFoundException("api.server.error.pet-not-found")
        );
        User currentUser = userRepository.findByEmail(principal.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("api.server.error.user-not-found")
        );
        checkIfUserHasAccessToPet(currentUser, pet);

        currentUser.getPets().remove(pet);
        petCleaner.detachUser(pet, currentUser);
        petCleaner.detachFeatures(pet);
        petCleaner.removePet(pet);
        return new DeletedPetDto(pet.getChipId(), pet.getStampId());
    }

    public List<PetInfoDto> petsList(Pageable pageable) {
        return petRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(petInfoDtoMapper)
                .toList();
    }

    private void checkIfUserHasAccessToPet(User user, Pet pet) {
        boolean userHasAccess = user.getPets()
                .stream()
                .anyMatch(i -> i.getChipId().equals(pet.getChipId()));
        if (!userHasAccess) {
            throw new IllegalAccessToPetException("api.server.error.does-not-have-access-to-pet");
        }
    }

    private boolean isChipIdParameter(String searchParameter) {
        return Pattern.matches("\\d{15}", searchParameter);
    }

    private String message(String property) { // TODO: maybe move this method to ErrorController
        return propertyResolver.resolve(property, Locale.getDefault());
    }
}
