package com.fst.ridebuddy.repositories;

import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.entities.Reservation;
import com.fst.ridebuddy.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationsRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.ride.id_ride = :rideId AND r.user.id_user = :userId AND r.status = :status")
    boolean existsReservation(Long rideId, Long userId, String status);

    @Query("SELECT r.user FROM Reservation r WHERE r.ride.id_ride = :rideId AND r.status = 'accepted'")
    List<AppUser> findUsersInRide(Long rideId);

}
