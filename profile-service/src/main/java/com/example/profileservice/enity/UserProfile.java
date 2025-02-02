package com.example.profileservice.enity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(value = "profile")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    @MongoId
    String id;

    @Indexed
    String userId;
    @Indexed
    String username;
    @Indexed
    String email;
    @Indexed
    String fullName;
//    LocalDate dob;
//    String city;
}
