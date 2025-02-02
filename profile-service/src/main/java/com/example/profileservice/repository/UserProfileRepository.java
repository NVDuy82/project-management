package com.example.profileservice.repository;

import com.example.profileservice.enity.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    Optional<UserProfile> findByUserId(String userId);

    @Query("{ '$or': [ { 'username': { '$regex': ?0, '$options': 'i' } }, { 'fullName': { '$regex': ?0, '$options': 'i' } } ] }")
    List<UserProfile> searchByUsernameOrFullName(String query);

    // Tìm kiếm theo email
    @Query("{ 'email': { '$regex': ?0, '$options': 'i' } }")
    List<UserProfile> searchByEmail(String email);
}
