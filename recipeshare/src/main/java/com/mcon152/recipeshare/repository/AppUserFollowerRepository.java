
package com.mcon152.recipeshare.repository;

import com.mcon152.recipeshare.domain.AppUser;
import com.mcon152.recipeshare.domain.AppUserFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserFollowerRepository extends JpaRepository<AppUserFollower, Long> {

    // Use the correct field name: "followedUser"
    List<AppUserFollower> findByFollowedUser(AppUser followedUser);
}