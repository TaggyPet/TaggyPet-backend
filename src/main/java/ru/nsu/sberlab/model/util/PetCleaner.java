package ru.nsu.sberlab.model.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.sberlab.model.entity.Pet;
import ru.nsu.sberlab.model.entity.User;
import ru.nsu.sberlab.dao.PetRepository;

@Service
@RequiredArgsConstructor
public class PetCleaner {
    private final PetRepository petRepository;

    /**
     * <p>This method detach user from pet's list of users.</p>
     *
     * @param pet  this parameter presents pet entity
     * @param user this parameter presents user entity
     */
    public void detachUser(Pet pet, User user) {
        pet.getUsers().remove(user);
    }

    /**
     * <p>This method detach all pet's features from pet.
     * Pet's features are removed when the user (owner of features) is deleted.
     * </p>
     *
     * @param pet this parameter presents pet entity
     */
    public void detachFeatures(Pet pet) {
        if (pet.getUsers().isEmpty()) {
            pet.getFeatures().forEach(feature -> feature.getPets().remove(pet));
            pet.getFeatures().clear();
        }
    }

    /**
     * <p>This method delete pet from database.</p>
     *
     * @param pet this parameter presents pet entity
     */
    @Transactional
    public void removePet(Pet pet) {
        if (pet.getUsers().isEmpty()) {
            petRepository.deleteByChipId(pet.getChipId());
        }
    }
}
