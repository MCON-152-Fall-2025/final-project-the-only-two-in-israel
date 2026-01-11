package com.mcon152.recipeshare.service;

import com.mcon152.recipeshare.domain.AppUser;
import com.mcon152.recipeshare.domain.AppUserFollower;
import com.mcon152.recipeshare.domain.Notification;
import com.mcon152.recipeshare.event.RecipeCreatedEvent;
import com.mcon152.recipeshare.repository.AppUserFollowerRepository;
import com.mcon152.recipeshare.repository.AppUserRepository;
import com.mcon152.recipeshare.repository.NotificationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AppUserRepository userRepository;
    private final AppUserFollowerRepository followerRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               AppUserRepository userRepository,
                               AppUserFollowerRepository followerRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

    @EventListener
    public void handleRecipeCreatedEvent(RecipeCreatedEvent event) {
        // Get author
        AppUser author = userRepository.findById(event.getAuthorId())
                .orElseThrow();

        // Notify all followers
        List<AppUserFollower> followers = followerRepository.findByFollowedUser(author);
        for (AppUserFollower followerLink : followers) {
            AppUser follower = followerLink.getFollower();
            Notification notification = new Notification(
                    follower,
                    "New recipe published by " + author.getUsername()
            );
            notificationRepository.save(notification);
        }
    }
} // <-- Make sure this closing brace exists