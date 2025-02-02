package com.example.historyservice.repository;

import com.example.historyservice.enity.ProjectHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectHistoryRepository extends JpaRepository<ProjectHistory, String> {
}
