package com.mcon152.recipeshare.web;

import com.mcon152.recipeshare.domain.Notification;
import com.mcon152.recipeshare.repository.NotificationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getNotificationsForUser(@PathVariable Long userId) {
        return notificationRepository.findAll()
                .stream()
                .filter(n -> n.getUser().getId().equals(userId))
                .toList();
    }
}
