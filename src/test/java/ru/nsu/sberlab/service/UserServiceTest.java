package ru.nsu.sberlab.service;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.sberlab.dao.DeletedUserRepository;
import ru.nsu.sberlab.dao.PetRepository;
import ru.nsu.sberlab.dao.UserRepository;
import ru.nsu.sberlab.model.dto.*;
import ru.nsu.sberlab.model.entity.DeletedUser;
import ru.nsu.sberlab.model.entity.Pet;
import ru.nsu.sberlab.model.entity.User;
import ru.nsu.sberlab.model.enums.Role;
import ru.nsu.sberlab.model.enums.Sex;
import ru.nsu.sberlab.model.mapper.PersonalCabinetDtoMapper;
import ru.nsu.sberlab.model.mapper.PetInfoDtoMapper;
import ru.nsu.sberlab.model.mapper.UserInfoDtoMapper;
import ru.nsu.sberlab.model.util.FeaturesConverter;
import ru.nsu.sberlab.model.util.PetCleaner;
import ru.nsu.sberlab.model.util.SocialNetworksConverter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DeletedUserRepository deletedUserRepository;
    @Mock
    private PetRepository petRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SocialNetworksConverter socialNetworksConverter;
    @Mock
    private FeaturesConverter featuresConverter;
    @Mock
    private PetInfoDtoMapper petInfoDtoMapper;
    @Mock
    private UserInfoDtoMapper userInfoDtoMapper;
    @Mock
    private PersonalCabinetDtoMapper personalCabinetDtoMapper;
    @Mock
    private PetCleaner petCleaner;
    @Mock
    private PropertyResolverUtils propertyResolver;

    @BeforeEach
    void setUp() {
        userService = new UserService(
                userRepository,
                deletedUserRepository,
                petRepository,
                passwordEncoder,
                socialNetworksConverter,
                featuresConverter,
                petInfoDtoMapper,
                userInfoDtoMapper,
                personalCabinetDtoMapper,
                petCleaner,
                propertyResolver
        );
    }

    @Test
    void itShouldCreateUser() {
        String email = "test@test";
        String phoneNumber = "+70001110000";
        String firstName = "John";
        String password = "ENCODED_PASSWORD";
        boolean hasPermitToShowPhoneNumber = true;
        boolean hasPermitToShowEmail = true;

        User user = new User(email, phoneNumber, firstName, password, hasPermitToShowPhoneNumber, hasPermitToShowEmail);
        /* This lines provides behavior before @PrePersist for invocation save method in user service */
        user.setUserId(null);
        user.setActive(false);
        user.setRole(null);

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(
                email,
                phoneNumber,
                firstName,
                password,
                true,
                true,
                Collections.emptyList()
        );

        given(userRepository.findByEmail(email))
                .willReturn(Optional.empty());

        given(passwordEncoder.encode(password))
                .willReturn("ENCODED_PASSWORD");

        given(userRepository.save(user))
                .willReturn(user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        userService.createUser(userRegistrationDto);

        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void itShouldUpdateInfoAboutUser() {
        long id = 1L;
        String email = "test@test";
        String phoneNumber = "+70001110000";
        String firstName = "John";
        String password = "ENCODED_PASSWORD";
        boolean hasPermitToShowPhoneNumber = true;
        boolean hasPermitToShowEmail = true;

        User user = new User(email, phoneNumber, firstName, password, hasPermitToShowPhoneNumber, hasPermitToShowEmail);
        user.setUserId(id);
        user.setActive(true);
        user.setRole(Role.ROLE_USER);

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));

        String newPhoneNumber = "+79990009999";
        String newFirstName = "William";
        boolean newPermitToShowEmailStatus = false;
        boolean newPermitToShowPhoneNumberStatus = true;
        String newPassword = Strings.EMPTY;
        List<SocialNetworkPostDto> newSocialNetworks = Collections.emptyList();
        UserEditDto userEditDto = new UserEditDto(
                newFirstName,
                email,
                newPhoneNumber,
                newPermitToShowEmailStatus,
                newPermitToShowPhoneNumberStatus,
                newPassword,
                newSocialNetworks
        );

        User updatedUser = new User(
                email,
                newPhoneNumber,
                newFirstName,
                newPassword,
                newPermitToShowPhoneNumberStatus,
                newPermitToShowEmailStatus
        );
        given(userRepository.save(user))
                .willReturn(updatedUser);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        userService.updateUserInfo(userEditDto, userService.loadUserByUsername(userEditDto.getEmail()));

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getPhoneNumber()).isEqualTo(newPhoneNumber);
        assertThat(capturedUser.getFirstName()).isEqualTo(newFirstName);
        assertThat(capturedUser.isHasPermitToShowEmail()).isEqualTo(newPermitToShowEmailStatus);
        assertThat(capturedUser.isHasPermitToShowPhoneNumber()).isEqualTo(newPermitToShowPhoneNumberStatus);
        assertThat(capturedUser.getUserSocialNetworks()).isEmpty();
    }

    @Test
    void itShouldDeleteUser() {
        long id = 1L;
        String email = "test@test";
        String phoneNumber = "+70001110000";
        String firstName = "John";
        String password = "test";
        boolean hasPermitToShowPhoneNumber = true;
        boolean hasPermitToShowEmail = true;

        User user = new User(email, phoneNumber, firstName, password, hasPermitToShowPhoneNumber, hasPermitToShowEmail);
        user.setUserId(id);
        user.setActive(true);
        user.setRole(Role.ROLE_USER);

        given(userRepository.findUserByUserId(id))
                .willReturn(Optional.of(user));

        ArgumentCaptor<DeletedUser> deletedUserArgumentCaptor = ArgumentCaptor.forClass(DeletedUser.class);
        userService.deleteUser(id);

        verify(deletedUserRepository).save(deletedUserArgumentCaptor.capture());
        DeletedUser deletedUser = deletedUserArgumentCaptor.getValue();
        assertThat(deletedUser.getEmail()).isEqualTo(user.getEmail());
        verify(userRepository).deleteByUserId(id);
    }

    @Test
    void itShouldCreatePet() {
        long id = 1L;
        String email = "test@test";
        String phoneNumber = "+70001110000";
        String firstName = "John";
        String password = "test";
        boolean hasPermitToShowPhoneNumber = true;
        boolean hasPermitToShowEmail = true;

        User user = new User(email, phoneNumber, firstName, password, hasPermitToShowPhoneNumber, hasPermitToShowEmail);
        user.setUserId(id);
        user.setActive(true);
        user.setRole(Role.ROLE_USER);

        PetCreationDto petCreationDto = new PetCreationDto(
                "999999999999999",
                "TTT 1111",
                "Pancake",
                "Cat",
                "Unknown",
                Sex.FEMALE,
                null
        );

        given(userRepository.findUserByUserId(id))
                .willReturn(Optional.of(user));

        assertThat(user.getPets()).isEmpty();
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        MultipartFile petImage = new MockMultipartFile("test", new byte[0]);
        userService.createPet(petCreationDto, petImage, user);

        verify(userRepository).findUserByUserId(id);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
        assertThat(user.getPets()).hasSize(1);
    }

    @Test
    void itShouldReturnPetsListByUserId() {
        long id = 1L;
        String email = "test@test";
        String phoneNumber = "+70001110000";
        String firstName = "John";
        String password = "test";
        boolean hasPermitToShowPhoneNumber = true;
        boolean hasPermitToShowEmail = true;

        User user = new User(email, phoneNumber, firstName, password, hasPermitToShowPhoneNumber, hasPermitToShowEmail);
        user.setUserId(id);
        user.setActive(true);
        user.setRole(Role.ROLE_USER);

        String chipId = "999999999999999";
        String stampId = "TTT 1111";
        String type = "Cat";
        String breed = "Unknown";
        Sex sex = Sex.FEMALE;
        String name = "Pancake";
        Pet pet = new Pet(chipId, stampId, type, breed, sex, name, Collections.emptyList(), null);
        user.setPets(List.of(pet));

        given(userRepository.findUserByUserId(id))
                .willReturn(Optional.of(user));

        given(petInfoDtoMapper.apply(pet))
                .willReturn(
                        new PetInfoDto(
                                pet.getChipId(),
                                pet.getStampId(),
                                pet.getName(),
                                pet.getType(),
                                pet.getBreed(),
                                pet.getSex(),
                                List.of(new UserCardDto(user.getFirstName(), user.getEmail(), user.getPhoneNumber(), Collections.emptyList())),
                                Collections.emptyList(),
                                null
                        )
                );

        ArgumentCaptor<Pet> petArgumentCaptor = ArgumentCaptor.forClass(Pet.class);
        List<PetInfoDto> petsList = userService.petsListByUserId(id);

        verify(userRepository).findUserByUserId(id);
        verify(petInfoDtoMapper).apply(petArgumentCaptor.capture());
        Pet capturedPet = petArgumentCaptor.getValue();
        assertThat(capturedPet).isEqualTo(pet);
        assertThat(petsList).hasSize(1);
    }

    @Test
    void itShouldReturnUserInfoByEmail() {
        long id = 1L;
        String email = "test@test";
        String phoneNumber = "+70001110000";
        String firstName = "John";
        String password = "test";
        boolean hasPermitToShowPhoneNumber = true;
        boolean hasPermitToShowEmail = true;

        User user = new User(email, phoneNumber, firstName, password, hasPermitToShowPhoneNumber, hasPermitToShowEmail);
        user.setUserId(id);
        user.setActive(true);
        user.setRole(Role.ROLE_USER);

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));

        UserInfoDto expectedUserInfo = new UserInfoDto(
                firstName,
                email,
                phoneNumber,
                hasPermitToShowEmail,
                hasPermitToShowPhoneNumber,
                Collections.emptyList()
        );

        given(userInfoDtoMapper.apply(user))
                .willReturn(expectedUserInfo);

        UserInfoDto userInfo = userService.getUserInfoDtoByEmail(email);
        verify(userRepository).findByEmail(email);
        verify(userInfoDtoMapper).apply(user);
        assertThat(userInfo).isEqualTo(expectedUserInfo);
    }

    @Test
    void itShouldReturnPersonalCabinetByEmail() {
        long id = 1L;
        String email = "test@test";
        String phoneNumber = "+70001110000";
        String firstName = "John";
        String password = "test";
        boolean hasPermitToShowPhoneNumber = true;
        boolean hasPermitToShowEmail = true;

        User user = new User(email, phoneNumber, firstName, password, hasPermitToShowPhoneNumber, hasPermitToShowEmail);
        user.setUserId(id);
        user.setActive(true);
        user.setRole(Role.ROLE_USER);

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));

        List<SocialNetworkOverviewDto> userSocialNetworks = List.of(new SocialNetworkOverviewDto("TG", "TG_USERNAME"));
        List<PetCardDto> petCards = List.of(new PetCardDto("111222333444555", null, "Pancake", null));
        PersonalCabinetDto expectedPersonalCabinet = new PersonalCabinetDto(
                firstName,
                email,
                phoneNumber,
                userSocialNetworks,
                petCards
        );

        given(personalCabinetDtoMapper.apply(user))
                .willReturn(expectedPersonalCabinet);

        PersonalCabinetDto personalCabinet = userService.getPersonalCabinetDtoByEmail(email);
        verify(userRepository).findByEmail(email);
        verify(personalCabinetDtoMapper).apply(user);
        assertThat(personalCabinet).isEqualTo(expectedPersonalCabinet);
    }
}
