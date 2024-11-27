package com.fst.ridebuddy.repositories;

import com.fst.ridebuddy.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationsRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.ride.id_ride = :rideId AND r.user.id_user = :userId AND r.status = :status")
    boolean existsReservation(Long rideId, Long userId, String status);
}
