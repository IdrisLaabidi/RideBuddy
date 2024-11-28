package com.fst.ridebuddy.services;

import com.fst.ridebuddy.entities.Ride;
import com.fst.ridebuddy.repositories.RidesRepository;
import com.fst.ridebuddy.utils.GeoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {

    private final RidesRepository rideRepository;

    @Autowired
    public RideService(RidesRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Transactional
    public Ride createRide(Ride ride) {
        return rideRepository.save(ride);
    }

    public Ride getRideById(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found with id: " + id));
    }

    public List<Ride> getAllRidesByUserId(Long userId) {
        return rideRepository.findAllByConductor_Id(userId);
    }

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }


    @Transactional
    public Ride updateRide(Long id, Ride updatedRide) {
        Ride existingRide = getRideById(id);
        existingRide.setDepartureLocation(updatedRide.getDepartureLocation());
        existingRide.setDepartureTime(updatedRide.getDepartureTime());
        existingRide.setAvailablePlaces(updatedRide.getAvailablePlaces());
        existingRide.setStatus(updatedRide.getStatus());
        existingRide.setComments(updatedRide.getComments());
        return rideRepository.save(existingRide);
    }

    @Transactional
    public void deleteRide(Long id) {
        if (!rideRepository.existsById(id)) {
            throw new IllegalArgumentException("Ride not found with id: " + id);
        }
        rideRepository.deleteById(id);
    }

    public List<Ride> getRidesSortedByProximity(double [] userLocation) {
        double userLatitude = userLocation[0];
        double userLongitude = userLocation[1];

        // Fetch all rides and sort by distance
        return rideRepository.findAllByStatus("in-progress").stream()
                .sorted(Comparator.comparingDouble(ride -> GeoUtils.calculateDistance(
                        userLatitude, userLongitude,
                        ride.parseCoordinates()[1],
                        ride.parseCoordinates()[0]
                )))
                .collect(Collectors.toList());
    }
}
