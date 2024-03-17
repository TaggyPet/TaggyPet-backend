package ru.nsu.sberlab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.sberlab.dao.FeaturePropertiesRepository;
import ru.nsu.sberlab.dao.PetRepository;
import ru.nsu.sberlab.dao.UserRepository;
import ru.nsu.sberlab.model.dto.FeatureCreationDto;
import ru.nsu.sberlab.model.dto.PetEditDto;
import ru.nsu.sberlab.model.dto.PetInfoDto;
import ru.nsu.sberlab.model.entity.Feature;
import ru.nsu.sberlab.model.entity.FeatureProperty;
import ru.nsu.sberlab.model.entity.Pet;
import ru.nsu.sberlab.model.entity.User;
import ru.nsu.sberlab.model.enums.Role;
import ru.nsu.sberlab.model.enums.Sex;
import ru.nsu.sberlab.model.mapper.PetEditDtoMapper;
import ru.nsu.sberlab.model.mapper.PetInfoDtoMapper;
import ru.nsu.sberlab.model.util.FeaturesConverter;
import ru.nsu.sberlab.model.util.PetCleaner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {
    private PetService petService;
    @Mock
    private PetRepository petRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FeaturePropertiesRepository featurePropertiesRepository;
    @Mock
    private PetInfoDtoMapper petInfoDtoMapper;
    @Mock
    private PetEditDtoMapper petEditDtoMapper;
    @Mock
    private FeaturesConverter featuresConverter;
    @Mock
    private PetCleaner petCleaner;
    @Mock
    private PropertyResolverUtils propertyResolver;

    @BeforeEach
    void setUp() {
        petService = new PetService(
                petRepository,
                userRepository,
                featurePropertiesRepository,
                petInfoDtoMapper,
                petEditDtoMapper,
                featuresConverter,
                petCleaner,
                propertyResolver
        );
    }

    @Test
    void itShouldReturnPetInfoByChipId() {
        String chipId = "999999999999999";
        String stampId = "TTT 1111";
        String type = "Cat";
        String breed = "Unknown";
        Sex sex = Sex.FEMALE;
        String name = "Pancake";
        Pet pet = new Pet(chipId, stampId, type, breed, sex, name, Collections.emptyList(), null);
        PetInfoDto expectedDto = new PetInfoDto(chipId, stampId, name, type, breed, sex, null, Collections.emptyList(), null);

        given(petRepository.findByChipId(chipId))
                .willReturn(Optional.of(pet));

        given(petInfoDtoMapper.apply(pet))
                .willReturn(expectedDto);

        ArgumentCaptor<Pet> petArgumentCaptor = ArgumentCaptor.forClass(Pet.class);
        PetInfoDto result = petService.getPetInfoBySearchParameter(chipId);
        verify(petRepository).findByChipId(chipId);
        verify(petInfoDtoMapper).apply(petArgumentCaptor.capture());
        Pet capturedPet = petArgumentCaptor.getValue();
        assertThat(capturedPet).isEqualTo(pet);
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void itShouldReturnPetInfoByStampId() {
        String chipId = "999999999999999";
        String stampId = "TTT 1111";
        String type = "Cat";
        String breed = "Unknown";
        Sex sex = Sex.FEMALE;
        String name = "Pancake";
        Pet pet = new Pet(chipId, stampId, type, breed, sex, name, Collections.emptyList(), null);
        PetInfoDto expectedDto = new PetInfoDto(chipId, stampId, name, type, breed, sex, null, Collections.emptyList(), null);

        given(petRepository.findByStampId(stampId))
                .willReturn(Optional.of(pet));

        given(petInfoDtoMapper.apply(pet))
                .willReturn(expectedDto);

        ArgumentCaptor<Pet> petArgumentCaptor = ArgumentCaptor.forClass(Pet.class);
        PetInfoDto result = petService.getPetInfoBySearchParameter(stampId);
        verify(petRepository).findByStampId(stampId);
        verify(petInfoDtoMapper).apply(petArgumentCaptor.capture());
        Pet capturedPet = petArgumentCaptor.getValue();
        assertThat(capturedPet).isEqualTo(pet);
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void itShouldReturnPetEditDtoByChipId() {
        String chipId = "999999999999999";
        String stampId = "TTT 1111";
        String type = "Cat";
        String breed = "Unknown";
        Sex sex = Sex.FEMALE;
        String name = "Pancake";
        Pet pet = new Pet(chipId, stampId, type, breed, sex, name, Collections.emptyList(), null);
        PetEditDto expectedDto = new PetEditDto(name, type, breed, sex, Collections.emptyList(), null);

        given(petRepository.findByChipId(chipId))
                .willReturn(Optional.of(pet));

        given(petEditDtoMapper.apply(pet, Collections.emptyList()))
                .willReturn(expectedDto);

        PetEditDto result = petService.getPetEditDtoByChipId(chipId);
        verify(petRepository).findByChipId(chipId);
        verify(petEditDtoMapper).apply(pet, Collections.emptyList());
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void itShouldUpdatePetInfo_withoutNewFeatures() {
        String chipId = "999999999999999";
        String stampId = "TTT 1111";
        String type = "Cat";
        String breed = "Unknown";
        Sex sex = Sex.FEMALE;
        String name = "Pancake";
        List<Feature> features = Collections.emptyList();
        Pet pet = new Pet(chipId, stampId, type, breed, sex, name, features, null);
        long petId = 1L;
        pet.setPetId(petId);

        given(petRepository.findById(petId))
                .willReturn(Optional.of(pet));

        String email = "test@test";
        String phoneNumber = "+70001110000";
        String firstName = "John";
        String password = "test";
        User user = new User(email, phoneNumber, firstName, password, true, true);
        user.setActive(true);
        user.setRole(Role.ROLE_USER);
        user.getPets().add(pet);

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));

        String newName = "Flay";
        List<FeatureCreationDto> newFeatures = Collections.emptyList();
        PetEditDto petEditDto = new PetEditDto(
                newName,
                type,
                breed,
                sex,
                newFeatures,
                null
        );

        MultipartFile petImage = new MockMultipartFile("test", new byte[0]);
        petService.updatePet(petId, petEditDto, petImage, user);
        verify(petRepository).findById(petId);
        verify(petRepository).save(pet);
        assertThat(pet.getName()).isEqualTo(newName);
    }

    @Test
    void itShouldUpdatePetInfo_withNewFeatures() {
        String chipId = "999999999999999";
        String stampId = "TTT 1111";
        String type = "Cat";
        String breed = "Unknown";
        Sex sex = Sex.FEMALE;
        String name = "Pancake";
        List<Feature> features = Collections.emptyList();
        Pet pet = new Pet(chipId, stampId, type, breed, sex, name, features, null);
        long petId = 1L;
        pet.setPetId(petId);

        given(petRepository.findById(petId))
                .willReturn(Optional.of(pet));

        String email = "test@test";
        String phoneNumber = "+70001110000";
        String firstName = "John";
        String password = "test";
        User user = new User(email, phoneNumber, firstName, password, true, true);
        user.setActive(true);
        user.setRole(Role.ROLE_USER);
        user.getPets().add(pet);

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));

        String newBreed = "British cat";
        String newFeatureDescription = "Allergy to apples";
        long newFeaturePropertyId = 0L;
        List<FeatureCreationDto> newFeatures = List.of(new FeatureCreationDto(newFeatureDescription, newFeaturePropertyId));
        PetEditDto petEditDto = new PetEditDto(
                name,
                type,
                newBreed,
                sex,
                newFeatures,
                null
        );

        FeatureProperty mockedProperty = new FeatureProperty();
        Feature mockedFeature = new Feature(newFeatureDescription, mockedProperty, user);
        given(featuresConverter.convertFeatureDtoListToFeatures(newFeatures, user))
                .willReturn(List.of(mockedFeature));

        MultipartFile petImage = new MockMultipartFile("test", new byte[0]);
        petService.updatePet(petId, petEditDto, petImage, user);
        verify(petRepository).findById(petId);
        verify(petRepository).save(pet);
        assertThat(pet.getBreed()).isEqualTo(newBreed);
        assertThat(pet.getFeatures()).hasSize(1);
    }

    @Test
    void itShouldDeletePet() {
        String chipId = "999999999999999";
        String stampId = "TTT 1111";
        String type = "Cat";
        String breed = "Unknown";
        Sex sex = Sex.FEMALE;
        String name = "Pancake";
        Pet pet = new Pet(chipId, stampId, type, breed, sex, name, Collections.emptyList(), null);
        long petId = 1L;
        pet.setPetId(petId);

        String email = "test@test";
        String phoneNumber = "+70001110000";
        String firstName = "John";
        String password = "test";
        User user = new User(email, phoneNumber, firstName, password, true, true);
        user.setActive(true);
        user.setRole(Role.ROLE_USER);
        user.getPets().add(pet);

        given(petRepository.findById(petId))
                .willReturn(Optional.of(pet));

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));

        petService.deletePet(petId, user);
        verify(petRepository).findById(petId);
        verify(userRepository).findByEmail(email);
        verify(petCleaner).removePet(pet);
        assertThat(user.getPets()).doesNotContain(pet);
    }

    @Test
    void itShouldReturnPetsList() {
        Pageable pageable = PageRequest.of(0, 10);

        given(petRepository.findAll(pageable))
                .willReturn(Page.empty());

        petService.petsList(pageable);
        verify(petRepository).findAll(pageable);
    }
}
