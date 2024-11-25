package com.fst.ridebuddy.services;

import com.fst.ridebuddy.entities.Ride;
import com.fst.ridebuddy.repositories.RidesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
}
