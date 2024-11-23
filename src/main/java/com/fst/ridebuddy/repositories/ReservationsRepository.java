package com.fst.ridebuddy.repositories;

import com.fst.ridebuddy.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationsRepository extends JpaRepository<Reservation, Long> {
}
