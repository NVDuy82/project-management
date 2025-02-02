package com.example.taskservice.repository;

import com.example.taskservice.enity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findAllByAssignedUserId(String userId);
    Page<Task> findAllByAssignedUserId(String userId, Pageable pageable);

    List<Task> findAllByProjectId(String projectId);

    List<Task> findAllByAssignedUserIdAndProjectId(String assignedUserId, String projectId);
}
