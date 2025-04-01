package com.buixuantruong.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "social_accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider", length = 100, nullable = false)
    private String provider;

    @Column(name = "provider_id", length = 100, nullable = false)
    private String providerId;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "email", length = 20)
    private String email;


}
