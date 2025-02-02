package com.example.projectservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_user_id", columnList = "user_id")
})
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    
    String userId;
    
    @ManyToOne
    @JoinColumn(name = "project_id")
    Project project;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;
}
