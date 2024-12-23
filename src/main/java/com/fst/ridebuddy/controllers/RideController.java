package com.fst.ridebuddy.controllers;

import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.entities.Ride;
import com.fst.ridebuddy.models.ReviewDto;
import com.fst.ridebuddy.models.RideDTO;
import com.fst.ridebuddy.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("/rides")
public class RideController {

    @Autowired
    private RideService rideService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private GeoCodingService geoCodingService;

    @Autowired
    private ReservationsService reservationsService;

    @Autowired
    private NotificationService notificationService;

    private final String apiKey = System.getenv("ORS_token");


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        // Make sure the RideDTO is added to the model
        model.addAttribute("RideDTO", new RideDTO());
        return "rides/createRide";
    }

    @PostMapping("/create")
    public String createRide(@ModelAttribute RideDTO rideDTO, Model model) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }

        LocalDateTime combinedDepartureTime = LocalDateTime.of(rideDTO.getDepartureDate(), rideDTO.getDepartureTime());
        combinedDepartureTime = combinedDepartureTime.plusHours(1);

        // If coordinates are provided, deduce locations using OpenRouteService API
        String departureLocation = rideDTO.getDepartureLocation();
        String destinationLocation = rideDTO.getDestination();
        String startCords = rideDTO.getStartCoordinate();
        String endCords = rideDTO.getEndCoordinate();

        String departure = "";
        String destination = "";

        if (rideDTO.getStartCoordinate() != null && rideDTO.getEndCoordinate() != null) {
            // Use OpenRouteService API to get addresses for coordinates
            departure = geoCodingService.getAddressFromCoordinates(rideDTO.getStartCoordinate());
            String [] departureArray = departure.split(",");
            departureLocation = departureArray[0] ;
            rideDTO.setStatGov( departureArray[1] );
            destination = geoCodingService.getAddressFromCoordinates(rideDTO.getEndCoordinate());
            String [] destinationArray = destination.split(",");
            destinationLocation = destinationArray[0] ;
            rideDTO.setEndGov( destinationArray[1] );
        }else{
            startCords = geoCodingService.getCoordinates(rideDTO.getDepartureLocation()+','+rideDTO.getStatGov()+",Tunisia");
            endCords = geoCodingService.getCoordinates(rideDTO.getDestination()+','+rideDTO.getEndGov()+",Tunisia");
        }

        // Create a new Ride entity and set its fields
        Ride ride = new Ride();
        ride.setDepartureLocation(departureLocation);
        ride.setDepartureTime(combinedDepartureTime);
        ride.setDestination(destinationLocation);
        ride.setAvailablePlaces(rideDTO.getAvailablePlaces());
        ride.setPricePerSeat(rideDTO.getPricePerSeat());
        ride.setStatus("in-progress"); // default status
        ride.setComments(rideDTO.getComments());
        ride.setStartCoordinate(startCords);
        ride.setEndCoordinate(endCords);
        ride.setStatGov(rideDTO.getStatGov());
        ride.setEndGov(rideDTO.getEndGov());
        ride.setConductor(user);


        // Save the ride using the service
        rideService.createRide(ride);
        return "redirect:/rides/myRides";
    }


    @GetMapping("/ride-visualize/{id}")
    public String visualizeRide (Model model, @PathVariable Long id) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        Ride ride = rideService.getRideById(id);
        List<AppUser> usersInRide = reservationsService.findUsersInRide(id) ;

        model.addAttribute("usersInRide", usersInRide);
        model.addAttribute("start", ride.getStartCoordinate());
        model.addAttribute("end", ride.getEndCoordinate());
        model.addAttribute("apiKey", apiKey);

        return "/rides/visualizeRide";
    }

    @GetMapping("/myRides")
    public String getMyRides(Model model) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        // Fetch all rides from the database
        List<Ride> allRides = rideService.getAllRidesByUserId(user.getId_user());

        // Add the rides to the model to pass them to the Thymeleaf template
        model.addAttribute("rides", allRides);

        return "rides/allRides";
    }

    @GetMapping("/all-rides")
    public String getAllRides(@RequestParam(value = "rideId", required = false) Long rideId, Model model) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);

            // Fetch all rides
            List<Ride> allRides = rideService.getAllRides();
            model.addAttribute("rides", allRides);

            // Fetch user reservations (ride IDs only)
            List<Long> userReservations = reservationsService.getReservationsByUser(user.getId_user())
                    .stream()
                    .map(reservation -> reservation.getRide().getId_ride())
                    .toList();
            model.addAttribute("userReservations", userReservations);

            if (rideId != null) {
                model.addAttribute("highlightRideId", rideId);
            }
        }

        return "rides/allRides"; // Return the view for displaying all rides
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

    @PostMapping("/manage/edit/{id}")
    public String updateRide(@PathVariable("id") Long id,
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
        List<AppUser> usersInRide = reservationsService.findUsersInRide(existingRide.getId_ride());
        notificationService.notifyRideEdited(usersInRide,existingRide);


        return "redirect:/rides/myRides";
    }

    @GetMapping("/manage/delete/{id}")
    public String deleteRide(@PathVariable("id") Long id, Model model) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        rideService.deleteRide(id);
        return "redirect:/rides/myRides";
    }

    @GetMapping("/rides-near-me")
    public String ridesNearMe (Model model) {
        model.addAttribute("getLocation" , true);
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
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
        List<RideDTO> rides = new ArrayList<>(List.of());
        for(Ride ride : ridesSorted){
            rides.add(RideMapper.toDTO(ride));
        }
        model.addAttribute("rides", rides);
        model.addAttribute("sortedRides", ridesSorted);
        return "rides/nearbyRides";

    }


    @PostMapping("/manage/make-over/{id}")
    public String updateState(@ModelAttribute("RideDTO") RideDTO rideDTO,
                              Model model , @PathVariable Long id
                              ) {
        // Ensure authenticated user info is added to the model
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        // Map RideDTO back to Ride entity
        Ride existingRide = rideService.getRideById(id);
        existingRide.setStatus("over");
        rideService.updateRide(existingRide.getId_ride(), existingRide);
        List<AppUser> usersInRide = reservationsService.findUsersInRide(existingRide.getId_ride());
        notificationService.notifyRideOver(usersInRide,existingRide);
        return "redirect:/rides/myRides";


    }

    @GetMapping("/ride-details/{id}")
    public String rideDetails (Model model, @PathVariable Long id) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        Ride ride = rideService.getRideById(id);
        model.addAttribute("ride",ride);
        model.addAttribute("apiKey", apiKey);
        if(Objects.equals(ride.getStatus(), "in-progress")){
            boolean hasUserReserved = reservationsService.existsReservation(ride.getId_ride(), user.getId_user(), "PENDING");
            model.addAttribute("hasUserReserved", hasUserReserved);
        }
        if(Objects.equals(ride.getStatus(), "over") && reservationsService.existsReservation(ride.getId_ride(), user.getId_user(), "ACCEPTED")){
            model.addAttribute("userCanRate", true);
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setRideId(ride.getId_ride());
            assert user != null;
            reviewDto.setReviewerId(user.getId_user());
            reviewDto.setReviewedId(ride.getConductor().getId_user());
            System.out.println(reviewDto);
            model.addAttribute("reviewDto", reviewDto );
        }

        return "/rides/ridedetails";
    }

    @GetMapping("/create-select")
    public String newRide (Model model) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        model.addAttribute("RideDTO", new RideDTO());
        model.addAttribute("apiKey", apiKey);
        return "rides/enhancedCreateRide";
    }

    @PostMapping("/manage/rideStatus/{id}")
    public String cancelRide(@PathVariable("id") Long id, Model model) {
        // Ensure authenticated user info is added to the model
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        // Map RideDTO back to Ride entity
        Ride existingRide = rideService.getRideById(id);
        existingRide.setStatus("canceled");
        rideService.updateRide(existingRide.getId_ride(), existingRide);
        return "redirect:/rides/myRides";
    }

}
