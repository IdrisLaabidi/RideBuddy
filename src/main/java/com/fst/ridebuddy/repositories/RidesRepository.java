package com.fst.ridebuddy.repositories;

import com.fst.ridebuddy.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RidesRepository extends JpaRepository<Ride, Long> {

    // Custom query to find all rides by the conductor's ID
    @Query("SELECT r FROM Ride r WHERE r.conductor.id_user = :conductorId")
    List<Ride> findAllByConductor_Id(Long conductorId);

    List<Ride> findAllByStatus(String status);
}
