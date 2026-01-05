package com.mcon152.recipeshare.service;

import com.mcon152.recipeshare.domain.AppUser;
import com.mcon152.recipeshare.domain.AppUserFollower;
import com.mcon152.recipeshare.domain.Notification;
import com.mcon152.recipeshare.event.RecipeCreatedEvent;
import com.mcon152.recipeshare.repository.AppUserFollowerRepository;
import com.mcon152.recipeshare.repository.AppUserRepository;
import com.mcon152.recipeshare.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    private NotificationRepository notificationRepository;
    private AppUserRepository userRepository;
    private AppUserFollowerRepository followerRepository;
    private NotificationService notificationService;

    private AppUser author;
    private AppUser follower1;

    @BeforeEach
    void setUp() {
        notificationRepository = mock(NotificationRepository.class);
        userRepository = mock(AppUserRepository.class);
        followerRepository = mock(AppUserFollowerRepository.class);

        notificationService = new NotificationService(notificationRepository, userRepository, followerRepository);

        // Setup test users
        author = new AppUser();
        author.setId(1L);
        author.setUsername("author");

        follower1 = new AppUser();
        follower1.setId(2L);
        follower1.setUsername("follower1");
    }

    @Test
    void whenRecipeCreated_followersReceiveNotifications() {
        // Mock repository behavior
        when(userRepository.findById(author.getId())).thenReturn(Optional.of(author));

        AppUserFollower followerLink = new AppUserFollower(follower1, author);
        when(followerRepository.findByFollowedUser(author)).thenReturn(List.of(followerLink));

        // Trigger the event
        RecipeCreatedEvent event = new RecipeCreatedEvent(10L, author.getId());
        notificationService.handleRecipeCreatedEvent(event);

        // Capture the notification saved
        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository, times(1)).save(captor.capture());

        Notification savedNotification = captor.getValue();
        assertEquals(follower1, savedNotification.getUser());
        assertEquals("New recipe published by author", savedNotification.getMessage());
    }

    @Test
    void whenRecipeCreated_multipleFollowersReceiveNotifications() {
        // Add a second follower
        AppUser follower2 = new AppUser();
        follower2.setId(3L);
        follower2.setUsername("follower2");

        // Mock repository behavior
        when(userRepository.findById(author.getId())).thenReturn(Optional.of(author));

        AppUserFollower followerLink1 = new AppUserFollower(follower1, author);
        AppUserFollower followerLink2 = new AppUserFollower(follower2, author);
        when(followerRepository.findByFollowedUser(author))
                .thenReturn(List.of(followerLink1, followerLink2));

        // Trigger the event
        RecipeCreatedEvent event = new RecipeCreatedEvent(11L, author.getId());
        notificationService.handleRecipeCreatedEvent(event);

        // Capture notifications
        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository, times(2)).save(captor.capture());

        List<Notification> savedNotifications = captor.getAllValues();

        assertEquals(2, savedNotifications.size());
        assertEquals(follower1, savedNotifications.get(0).getUser());
        assertEquals("New recipe published by author", savedNotifications.get(0).getMessage());
        assertEquals(follower2, savedNotifications.get(1).getUser());
        assertEquals("New recipe published by author", savedNotifications.get(1).getMessage());
    }

    @Test
    void whenRecipeCreated_noFollowers_noNotificationsCreated() {
        // Mock repository behavior
        when(userRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(followerRepository.findByFollowedUser(author)).thenReturn(List.of());

        // Trigger the event
        RecipeCreatedEvent event = new RecipeCreatedEvent(12L, author.getId());
        notificationService.handleRecipeCreatedEvent(event);

        // No notifications should be saved
        verify(notificationRepository, never()).save(any());
    }
}