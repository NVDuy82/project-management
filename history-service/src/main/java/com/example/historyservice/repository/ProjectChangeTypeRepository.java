package com.example.historyservice.repository;

import com.example.historyservice.enity.ProjectChangeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectChangeTypeRepository extends JpaRepository<ProjectChangeType, String> {
}