package com.fst.ridebuddy.services;

import com.fst.ridebuddy.entities.Reservation;
import com.fst.ridebuddy.entities.Ride;
import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.repositories.ReservationsRepository;
import com.fst.ridebuddy.repositories.RidesRepository;
import com.fst.ridebuddy.repositories.AppUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationsService {

    @Autowired
    private ReservationsRepository reservationsRepository;

    @Autowired
    private RidesRepository ridesRepository;

    @Autowired
    private AppUsersRepository appUsersRepository;

    @Autowired
    private RideService rideService;

    // Fetch reservations by user ID
    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationsRepository.findAll().stream()
                .filter(reservation -> reservation.getUser().getId_user().equals(userId))
                .toList();
    }
    public List<Reservation> getReservationsByConductor(Long userId) {
        return reservationsRepository.findAll().stream()
                .filter(reservation -> reservation.getRide().getConductor().getId_user().equals(userId))
                .toList();
    }
    // Fetch reservation by ID
    public Reservation getReservationById(Long id) {
        return reservationsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    }

    // Cancel a reservation
    public void cancelReservation(Long id) {

        Reservation reservation = getReservationById(id);
        reservation.setStatus("CANCELED");

        Long ride_id = reservation.getRide().getId_ride();

        Ride existingRide = rideService.getRideById(ride_id);
        Integer reservedPlaces = reservation.getReservedPlaces();
        Integer availablePlaces = existingRide.getAvailablePlaces();
        existingRide.setAvailablePlaces(availablePlaces + reservedPlaces);
        rideService.updateRide(existingRide.getId_ride(), existingRide);

        ridesRepository.save(existingRide);
        reservationsRepository.save(reservation);
    }

    // Update a reservation
    public void updateReservation(Long reservationId, int reservedPlaces) {
        Reservation reservation = getReservationById(reservationId);
        reservation.setReservedPlaces(reservedPlaces);
        reservationsRepository.save(reservation);
    }

    public void updateReservationStatus(Long reservationId, String status) {
        Reservation reservation = getReservationById(reservationId);
        reservation.setStatus(status);
        reservationsRepository.save(reservation);
    }

    // Create a new reservation
    public Reservation createReservation(Long rideId, Long userId, int reservedPlaces) {
        // Fetch the ride by ID
        Ride ride = ridesRepository.findById(rideId)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found"));

        // Check if there are enough available places
        if (ride.getAvailablePlaces() < reservedPlaces) {
            throw new IllegalArgumentException("Not enough available places");
        }

        // Fetch the user by ID
        AppUser user = appUsersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        // Create and save the reservation
        Reservation reservation = new Reservation();
        reservation.setRide(ride);
        reservation.setUser(user);
        reservation.setReservedPlaces(reservedPlaces);
        reservation.setStatus("PENDING"); // Example status; can be adjusted based on business logic

        return reservationsRepository.save(reservation);
    }

    public boolean existsReservation(Long rideId, Long userId, String status) {
        return reservationsRepository.existsReservation(rideId, userId, status);
    }

    public List<AppUser> findUsersInRide(Long rideId){
        return reservationsRepository.findUsersInRide(rideId);
    }
}
