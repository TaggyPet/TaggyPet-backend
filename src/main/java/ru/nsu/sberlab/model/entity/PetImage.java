package ru.nsu.sberlab.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pet_images")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class PetImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "image_uuid_name")
    private String imageUUIDName;

    @Column(name = "image_size")
    private Long size;

    @Column(name = "image_type")
    private String contentType;

    @Lob
    @Column(name = "image_data")
    @EqualsAndHashCode.Exclude
    private byte[] imageData;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "petImage")
    private Pet pet;
}
