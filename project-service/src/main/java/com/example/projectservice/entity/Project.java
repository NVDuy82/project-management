package com.example.projectservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) 
    String id;

    @Column(nullable = false)
    String name;
    String description;

    Instant createdAt;
    Instant updatedAt;

    String createdBy;

    @ManyToOne
    @JoinColumn(name = "status", nullable = false)
    Status status;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    Set<Member> members;
}
