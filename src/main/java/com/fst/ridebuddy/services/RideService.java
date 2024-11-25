package com.fst.ridebuddy.services;

import com.fst.ridebuddy.entities.Ride;
import com.fst.ridebuddy.repositories.RidesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
