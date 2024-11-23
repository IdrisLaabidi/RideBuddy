package com.fst.ridebuddy.repositories;

import com.fst.ridebuddy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User,Long> {
}
