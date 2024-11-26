package com.fst.ridebuddy.controllers;

import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.entities.Ride;
import com.fst.ridebuddy.models.RideDTO;
import com.fst.ridebuddy.services.AppUserService;
import com.fst.ridebuddy.services.GeoCodingService;
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

    @GetMapping("/rides-near-me")
    public String ridesNearMe (Model model) {
        model.addAttribute("getLocation" , true);
        return "rides/nearbyRides";
    }

    @GetMapping("/rides-near-me/{cords}")
    public String ridesNearMe (Model model, @PathVariable String cords) {
        model.addAttribute("getLocation" , false);
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        String [] cordsArray = cords.split(",");
        double lat = Double.parseDouble(cordsArray[0]);
        double lon = Double.parseDouble(cordsArray[1]);
        double [] userCords = {lat,lon};
        List<Ride> ridesSorted = rideService.getRidesSortedByProximity(userCords);
        model.addAttribute("sortedRides", ridesSorted);
        return "rides/nearbyRides";

    }



}
