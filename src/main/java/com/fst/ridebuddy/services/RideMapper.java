package com.fst.ridebuddy.services;

import com.fst.ridebuddy.models.RideDTO;
import com.fst.ridebuddy.entities.Ride;

public class RideMapper {

    // Convert Ride entity to RideDTO
    public static RideDTO toDTO(Ride ride) {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setId_ride(ride.getId_ride());
        rideDTO.setDepartureLocation(ride.getDepartureLocation());
        rideDTO.setDepartureDate(ride.getDepartureTime().toLocalDate()); // Extract date
        rideDTO.setDepartureTime(ride.getDepartureTime().toLocalTime()); // Extract time
        rideDTO.setDestination(ride.getDestination());
        rideDTO.setAvailablePlaces(ride.getAvailablePlaces());
        rideDTO.setPricePerSeat(ride.getPricePerSeat());
        rideDTO.setStatus(ride.getStatus());
        rideDTO.setComments(ride.getComments());
        rideDTO.setStatGov(ride.getStatGov());
        rideDTO.setEndGov(ride.getEndGov());
        rideDTO.setStartCoordinate(ride.getStartCoordinate());
        rideDTO.setEndCoordinate(ride.getEndCoordinate());
        return rideDTO;
    }


}