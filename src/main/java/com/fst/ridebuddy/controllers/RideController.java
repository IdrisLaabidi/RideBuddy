package com.fst.ridebuddy.controllers;

import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.entities.Ride;
import com.fst.ridebuddy.models.RideDTO;
import com.fst.ridebuddy.services.AppUserService;
import com.fst.ridebuddy.services.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/rides")
public class RideController {

    @Autowired
    private RideService rideService;

    @Autowired
    private AppUserService appUserService; // Inject the AppUserService

    // Show the form for creating a ride
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // Make sure the RideDTO is added to the model
        model.addAttribute("RideDTO", new RideDTO());
        return "rides/createRide";
    }

    // Handle form submission to create a ride
    @PostMapping("/create")
    public String createRide(@ModelAttribute RideDTO rideDTO) {

        // Combine departureDate and departureTime into LocalDateTime
        LocalDateTime combinedDepartureTime = LocalDateTime.of(rideDTO.getDepartureDate(), rideDTO.getDepartureTime());

        // Add one hour to the departure time
        combinedDepartureTime = combinedDepartureTime.plusHours(1);

        // Create a new Ride entity and set its fields
        Ride ride = new Ride();
        ride.setDepartureLocation(rideDTO.getDepartureLocation());
        ride.setDepartureTime(combinedDepartureTime); // Set combined departureTime
        ride.setDestination(rideDTO.getDestination());
        ride.setAvailablePlaces(rideDTO.getAvailablePlaces());
        ride.setPricePerSeat(rideDTO.getPricePerSeat());
        ride.setStatus("in-progress"); // default status
        ride.setComments(rideDTO.getComments());

        // Retrieve the logged-in user from the security context
        AppUser loggedInUser = appUserService.getAuthenticatedUser();
        if (loggedInUser != null) {
            ride.setConductor(loggedInUser);
        } else {
            // Handle the case where no user is logged in
            throw new RuntimeException("User is not authenticated");
        }

        // Save the ride using the service
        rideService.createRide(ride);
        return "index";
    }



}
