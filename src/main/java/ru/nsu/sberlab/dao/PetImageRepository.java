package ru.nsu.sberlab.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.sberlab.model.entity.PetImage;

import java.util.Optional;

@Repository
public interface PetImageRepository extends JpaRepository<PetImage, Long> {
    Optional<PetImage> findByImageUUIDName(String uuidName);
}
