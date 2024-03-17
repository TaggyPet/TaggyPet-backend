package ru.nsu.sberlab.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "deleted_users")
@Data
@NoArgsConstructor
public class DeletedUser {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    @Column(name = "date_of_deletion")
    private LocalDateTime dateOfDeletion;

    public DeletedUser(Long userId, String email, String phoneNumber, String firstName, LocalDateTime dateOfCreation) {
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.dateOfCreation = dateOfCreation;
    }

    @PrePersist
    private void initialization() {
        dateOfDeletion = LocalDateTime.now();
    }
}
