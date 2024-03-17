package ru.nsu.sberlab.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.nsu.sberlab.model.entity.Pet;
import ru.nsu.sberlab.model.enums.Sex;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class PetRepositoryTest {
    @Autowired
    private PetRepository petRepository;

    @AfterEach
    void tearDown() {
        petRepository.deleteAll();
    }

    @Test
    void itShouldFindPetByChipId() {
        String chipId = "999999999999999";
        String stampId = "TTT 1111";
        String type = "Cat";
        String breed = "Unknown";
        Sex sex = Sex.FEMALE;
        String name = "Pancake";
        Pet pet = new Pet(chipId, stampId, type, breed, sex, name, null, null);
        petRepository.save(pet);

        Optional<Pet> result = petRepository.findByChipId(chipId);

        assertThat(result).isPresent();
    }

    @Test
    void itShouldFindPetByStampId() {
        String chipId = "999999999999999";
        String stampId = "TTT 1111";
        String type = "Cat";
        String breed = "Unknown";
        Sex sex = Sex.FEMALE;
        String name = "Pancake";
        Pet pet = new Pet(chipId, stampId, type, breed, sex, name, null, null);
        petRepository.save(pet);

        Optional<Pet> result = petRepository.findByStampId(stampId);

        assertThat(result).isPresent();
    }

    @Test
    void itShouldDoesntFindPet() {
        String chipId = "999999999999999";
        String stampId = "TTT 1111";

        Optional<Pet> resultByChipId = petRepository.findByStampId(chipId);
        Optional<Pet> resultByStampId = petRepository.findByStampId(stampId);

        assertThat(resultByChipId).isEmpty();
        assertThat(resultByStampId).isEmpty();
    }

    @Test
    void itShouldDeletePetByChipId() {
        String chipId = "999999999999999";
        String stampId = "TTT 1111";
        String type = "Cat";
        String breed = "Unknown";
        Sex sex = Sex.FEMALE;
        String name = "Pancake";
        Pet pet = new Pet(chipId, stampId, type, breed, sex, name, null, null);
        petRepository.save(pet);
        petRepository.deleteByChipId(chipId);

        Optional<Pet> result = petRepository.findByChipId(chipId);

        assertThat(result).isEmpty();
    }
}
