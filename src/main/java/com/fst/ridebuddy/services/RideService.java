package com.fst.ridebuddy.services;

import com.fst.ridebuddy.entities.Ride;
import java.util.List;

public interface RideService {
    Ride createRide(Ride ride);
    Ride getRideById(Long id);
    List<Ride> getAllRides();
    Ride updateRide(Long id, Ride updatedRide);
    void deleteRide(Long id);
}
