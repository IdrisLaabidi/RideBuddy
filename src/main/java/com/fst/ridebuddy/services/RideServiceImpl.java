package com.fst.ridebuddy.services;

import com.fst.ridebuddy.entities.Ride;
import com.fst.ridebuddy.repositories.RidesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fst.ridebuddy.utils.GeoUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RideServiceImpl implements RideService {

    private final RidesRepository rideRepository;

    public RideServiceImpl(RidesRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Transactional
    public Ride createRide(Ride ride) {
        return rideRepository.save(ride);
    }

    @Override
    public Ride getRideById(Long id) {
        return rideRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ride not found with id: " + id));
    }

    @Override
    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    @Override
    public Ride updateRide(Long id, Ride updatedRide) {
        Ride existingRide = getRideById(id);
        existingRide.setDepartureLocation(updatedRide.getDepartureLocation());
        existingRide.setDepartureTime(updatedRide.getDepartureTime());
        existingRide.setAvailablePlaces(updatedRide.getAvailablePlaces());
        existingRide.setStatus(updatedRide.getStatus());
        existingRide.setComments(updatedRide.getComments());
        return rideRepository.save(existingRide);
    }

    @Override
    public void deleteRide(Long id) {
        rideRepository.deleteById(id);
    }

    @Override
    public List<Ride> getRidesSortedByProximity(double [] userLocation) {
        double userLatitude = userLocation[0];
        double userLongitude = userLocation[1];

        // Fetch all rides and sort by distance
        return rideRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(ride -> GeoUtils.calculateDistance(
                        userLatitude, userLongitude,
                        ride.parseCoordinates()[1],
                        ride.parseCoordinates()[0]
                )))
                .collect(Collectors.toList());
    }
}
