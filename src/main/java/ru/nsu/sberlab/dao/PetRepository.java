package ru.nsu.sberlab.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.sberlab.model.entity.Pet;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findByChipId(String chipId);
    Optional<Pet> findByStampId(String stampId);
    void deleteByChipId(String chipId);
}
