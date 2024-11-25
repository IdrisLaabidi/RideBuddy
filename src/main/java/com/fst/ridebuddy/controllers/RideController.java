package com.fst.ridebuddy.controllers;

import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.entities.Ride;
import com.fst.ridebuddy.models.RideDTO;
import com.fst.ridebuddy.services.AppUserService;
import com.fst.ridebuddy.services.GeoCodingService;
import com.fst.ridebuddy.services.RideService;
import com.fst.ridebuddy.services.RideMapper;
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
    private AppUserService appUserService;

    @Autowired
    private GeoCodingService geoCodingService;

    private final String apiKey = System.getenv("ORS_token");


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // Make sure the RideDTO is added to the model
        model.addAttribute("RideDTO", new RideDTO());
        return "rides/createRide";
    }

    @PostMapping("/create")
    public String createRide(@ModelAttribute RideDTO rideDTO) {

        LocalDateTime combinedDepartureTime = LocalDateTime.of(rideDTO.getDepartureDate(), rideDTO.getDepartureTime());

        combinedDepartureTime = combinedDepartureTime.plusHours(1);

        String startCords = geoCodingService.getCoordinates(rideDTO.getDepartureLocation()+','+rideDTO.getStatGov()+",Tunisia");
        String endCords = geoCodingService.getCoordinates(rideDTO.getDestination()+','+rideDTO.getEndGov()+",Tunisia");


        // Create a new Ride entity and set its fields
        Ride ride = new Ride();
        ride.setDepartureLocation(rideDTO.getDepartureLocation());
        ride.setDepartureTime(combinedDepartureTime); // Set combined departureTime
        ride.setDestination(rideDTO.getDestination());
        ride.setAvailablePlaces(rideDTO.getAvailablePlaces());
        ride.setPricePerSeat(rideDTO.getPricePerSeat());
        ride.setStatus("in-progress"); // default status
        ride.setComments(rideDTO.getComments());
        ride.setStartCoordinate(startCords);
        ride.setEndCoordinate(endCords);
        ride.setStatGov(rideDTO.getStatGov());
        ride.setEndGov(rideDTO.getEndGov());

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
        return "redirect:/";
    }

    @GetMapping("/ride-details/{id}")
    public String rideDetail (Model model, @PathVariable Long id) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        Ride ride = rideService.getRideById(id);
        model.addAttribute("start", ride.getStartCoordinate());
        model.addAttribute("end", ride.getEndCoordinate());
        model.addAttribute("apiKey", apiKey);

        return "rides/rideDetails";
    }

    @GetMapping("/manage")
    public String getAllRides(RideDTO rideDTO ,Model model) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        List<Ride> rides = rideService.getAllRides();
        model.addAttribute("rides", rides);
        return "rides/manageRides";
    }

    @GetMapping("/manage/edit/{id}")
    public String showEditRidePage(@PathVariable("id") Long id, Model model) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }

        // Fetch the ride entity and map it to RideDTO
        Ride ride = rideService.getRideById(id);
        RideDTO rideDTO = RideMapper.toDTO(ride);

        // Add RideDTO to the model
        model.addAttribute("RideDTO", rideDTO);

        return "rides/editRide"; // Render the edit ride page
    }

    @PostMapping("/manage/edit")
    public String updateRide(
            @ModelAttribute("RideDTO") RideDTO rideDTO,
            Model model
    ) {
        // Ensure authenticated user info is added to the model
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }

        // Map RideDTO back to Ride entity
        Ride existingRide = rideService.getRideById(rideDTO.getId_ride());

        // Update fields in the existing entity
        existingRide.setDepartureLocation(rideDTO.getDepartureLocation());
        existingRide.setDestination(rideDTO.getDestination());
        existingRide.setDepartureTime(
                LocalDateTime.of(rideDTO.getDepartureDate(), rideDTO.getDepartureTime()));
        existingRide.setAvailablePlaces(rideDTO.getAvailablePlaces());
        existingRide.setPricePerSeat(rideDTO.getPricePerSeat());
        existingRide.setComments(rideDTO.getComments());
        existingRide.setStartCoordinate(geoCodingService.getCoordinates(
                rideDTO.getDepartureLocation() + "," + rideDTO.getStatGov() + ",Tunisia"));
        existingRide.setEndCoordinate(geoCodingService.getCoordinates(
                rideDTO.getDestination() + "," + rideDTO.getEndGov() + ",Tunisia"));
        existingRide.setStatGov(rideDTO.getStatGov());
        existingRide.setEndGov(rideDTO.getEndGov());
        // Update the ride in the database
        rideService.updateRide(existingRide.getId_ride(), existingRide);

        // Redirect to home page after successful update
        return "redirect:/rides/manage";
    }


    @GetMapping("/manage/delete/{id}")
    public String deleteRide(@PathVariable("id") Long id, Model model) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        rideService.deleteRide(id);
        return "redirect:/rides/manage";
    }

}
