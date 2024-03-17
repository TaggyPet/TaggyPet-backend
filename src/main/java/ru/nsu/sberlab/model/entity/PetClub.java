package ru.nsu.sberlab.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pet_clubs")
@Data
@NoArgsConstructor
public class PetClub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long clubId;

    @Column(name = "club_name")
    private String clubName;

    @Column(name = "club_prefix")
    private String clubPrefix;
}

// TODO: add functionality that allows to get club info by pet stampId