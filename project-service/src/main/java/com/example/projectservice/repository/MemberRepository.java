package com.example.projectservice.repository;

import com.example.projectservice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    List<Member> findAllByUserId(String userId);
    List<Member> findAllByProject_Id(String projectId);
    Optional<Member> findByUserIdAndProject_Id(String userId, String projectId);
    boolean existsByUserIdAndProject_Id(String userId, String projectId);
}