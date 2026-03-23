package com.ankush.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ankush.model.Notification;
import com.ankush.repository.NotificationRepository;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    // Get notifications for a user
    @GetMapping("/{userId}")
    public List<Notification> getUserNotifications(@PathVariable Long userId) {

        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}