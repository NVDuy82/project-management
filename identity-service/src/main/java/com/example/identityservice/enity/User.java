package com.example.identityservice.enity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, unique = true)
    String username;

    @Column(nullable = false)
    String passwordHash;

    @Column(nullable = false, unique = true)
    String email;

    Instant createAt;
    
    @ManyToOne(fetch = FetchType.EAGER)
    Status status;

    @ManyToMany
    Set<Role> roles;
}
