package com.fst.ridebuddy.repositories;

import com.fst.ridebuddy.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppUsersRepository extends JpaRepository<AppUser,Long> {
    public AppUser findByEmail(String email);

    @Query("SELECT u FROM AppUser u WHERE u.emailVerificationToken = :token")
    public AppUser findByEmailVerificationToken(String token);
}
