package ru.nsu.sberlab.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users_social_networks")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class UserSocialNetwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_network_id")
    private Long socialNetworkId;

    @Column(name = "short_name")
    private String shortName;

    @ManyToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    @EqualsAndHashCode.Exclude
    private SocialNetwork socialNetwork;

    @ManyToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private User user;

    public UserSocialNetwork(String shortName, SocialNetwork socialNetwork, User principal) {
        this.shortName = shortName;
        this.socialNetwork = socialNetwork;
        this.user = principal;
    }
}
