package com.fst.ridebuddy.repositories;

import com.fst.ridebuddy.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUsersRepository extends JpaRepository<AppUser,Long> {
    public AppUser findByEmail(String email);
}
