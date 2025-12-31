package com.mcon152.recipeshare.service;

import com.mcon152.recipeshare.domain.AppUser;
import com.mcon152.recipeshare.domain.AppUserFollower;
import com.mcon152.recipeshare.domain.Notification;
import com.mcon152.recipeshare.event.RecipeCreatedEvent;
import com.mcon152.recipeshare.repository.AppUserRepository;
import com.mcon152.recipeshare.repository.NotificationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AppUserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               AppUserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @EventListener
    public void handleRecipeCreatedEvent(RecipeCreatedEvent event) {
        // Get author
        AppUser author = userRepository.findById(event.getAuthorId())
                .orElseThrow();

        // Notify all followers
        for (AppUserFollower followerLink : author.getFollowers()) {
            AppUser follower = followerLink.getFollower(); // extract actual user
            Notification notification = new Notification(
                    follower,
                    "New recipe published by " + author.getUsername()
            );
            notificationRepository.save(notification);
        }
    }
}