package com.example.taskservice.enity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_assigned_user_id", columnList = "assigned_user_id"),
        @Index(name = "idx_project_id", columnList = "project_id")
})
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) 
    String id;

    @Column(nullable = false)
    String projectId;

    @Column(nullable = false)
    String name;
    String description;

    Instant createdAt;
    Instant updatedAt;

    String assignedUserId;

    @ManyToOne
    @JoinColumn(name = "status", nullable = false)
    Status status;

}
