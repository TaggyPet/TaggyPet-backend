package ru.nsu.sberlab.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.nsu.sberlab.model.entity.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void itShouldFindUserByEmail() {
        String email = "test@test";
        String phoneNumber = "+70001110000";
        String firstName = "John";
        String password = "test";
        User user = new User(email, phoneNumber, firstName, password, true, true);
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail(email);

        assertThat(result).isPresent();
    }

    @Test
    void itShouldDoesNotFindUserByEmail() {
        String email = "unknown@test";

        Optional<User> result = userRepository.findByEmail(email);

        assertThat(result).isEmpty();
    }

    @Test
    void itShouldFindUserByUserId() {
        String email = "test@test";
        String phoneNumber = "+70001110000";
        String firstName = "John";
        String password = "test";
        User user = new User(email, phoneNumber, firstName, password, true, true);
        user = userRepository.save(user);

        Optional<User> result = userRepository.findUserByUserId(user.getUserId());

        assertThat(result).isPresent();
    }

    @Test
    void itShouldDeleteUserByUserId() {
        String email = "test@test";
        String phoneNumber = "+70001110000";
        String firstName = "John";
        String password = "test";
        User user = new User(email, phoneNumber, firstName, password, true, true);
        userRepository.save(user);
        userRepository.deleteByUserId(user.getUserId());

        Optional<User> result = userRepository.findByEmail(email);

        assertThat(result).isEmpty();
    }
}
