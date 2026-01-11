package com.mcon152.recipeshare.domain;

import jakarta.persistence.*;

@Entity
public class AppUserFollower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AppUser follower;

    @ManyToOne
    private AppUser followedUser;

    public AppUserFollower() {}

    public AppUserFollower(AppUser follower, AppUser followedUser) {
        this.follower = follower;
        this.followedUser = followedUser;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AppUser getFollower() { return follower; }
    public void setFollower(AppUser follower) { this.follower = follower; }

    public AppUser getFollowedUser() { return followedUser; }
    public void setFollowedUser(AppUser followedUser) { this.followedUser = followedUser; }
}
