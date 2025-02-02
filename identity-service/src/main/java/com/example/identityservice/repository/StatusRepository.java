package com.example.identityservice.repository;

import com.example.identityservice.enity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, String> {
    Optional<Status> findByName(String name);
}
