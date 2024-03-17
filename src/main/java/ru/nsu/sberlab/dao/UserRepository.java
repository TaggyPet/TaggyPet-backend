package ru.nsu.sberlab.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.sberlab.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    void deleteByUserId(Long userId);
    Optional<User> findByEmail(String email);
    Optional<User> findUserByUserId(Long userId);
}
