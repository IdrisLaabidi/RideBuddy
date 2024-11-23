package com.fst.ridebuddy.repositories;

import com.fst.ridebuddy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User,Long> {
    Optional<org.springframework.security.core.userdetails.User> findByEmail(String email);
}
