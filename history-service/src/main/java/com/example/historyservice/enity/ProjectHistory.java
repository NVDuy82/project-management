package com.example.historyservice.enity;

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
        @Index(name = "idx_project_id", columnList = "project_id")
})
public class ProjectHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) 
    String id;

    String projectId;
    String content;

    @ManyToOne
    ProjectChangeType type;

    Instant timestamp;
}
