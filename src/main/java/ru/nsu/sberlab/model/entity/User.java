package ru.nsu.sberlab.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.nsu.sberlab.model.enums.Role;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_seq", initialValue = 3, allocationSize = 1)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "active")
    private boolean active;

    @Column(name = "password", length = 128)
    private String password;

    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreated;

    @Column(name = "permission_to_show_phone_number")
    private boolean hasPermitToShowPhoneNumber;

    @Column(name = "permission_to_show_email")
    private boolean hasPermitToShowEmail;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_pets",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "pet_id")}
    )
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    }, fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true)
    private List<UserSocialNetwork> userSocialNetworks = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<Feature> features = new ArrayList<>();

    @PrePersist
    private void initialization() {
        this.role = Role.ROLE_USER;
        this.active = true;
        this.dateOfCreated = LocalDateTime.now();
    }

    public User(
            String email,
            String phoneNumber,
            String firstName,
            String password,
            boolean hasPermitToShowPhoneNumber,
            boolean hasPermitToShowEmail
    ) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.password = password;
        this.hasPermitToShowPhoneNumber = hasPermitToShowPhoneNumber;
        this.hasPermitToShowEmail = hasPermitToShowEmail;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
