package com.ankush.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ankush.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

}