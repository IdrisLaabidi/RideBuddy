package com.fst.ridebuddy.repositories;

import com.fst.ridebuddy.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RidesRepository extends JpaRepository<Ride,Long> {
}
