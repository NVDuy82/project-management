package com.example.projectservice.repository;

import com.example.projectservice.entity.Member;
import com.example.projectservice.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findAllByMembersContains(Member member);

    @Query("SELECT p FROM Project p JOIN p.members m WHERE m.userId = :userId")
    Page<Project> findByUserId(String userId, Pageable pageable);
}
