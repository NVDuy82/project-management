package com.example.notificationservice.repository;

import com.example.notificationservice.enity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findAllByToUserId(String userId);
    Page<Notification> findAllByToUserId(String userId, Pageable pageable);
}
