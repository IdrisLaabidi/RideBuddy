package com.fst.ridebuddy.controllers;

import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.entities.Reservation;
import com.fst.ridebuddy.models.ReservationDto;
import com.fst.ridebuddy.services.AppUserService;
import com.fst.ridebuddy.services.ReservationsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationsController {

    @Autowired
    private ReservationsService reservationsService;

    @Autowired
    private AppUserService appUserService;

    // Load the reservation management page
    @GetMapping("/manage")
    public String manageReservations(@RequestParam(value = "rideId", required = false) Long rideId, Model model) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        // Add a new reservationDto to the model
        ReservationDto reservationDto = new ReservationDto();
        if (rideId != null) {
            reservationDto.setRideId(rideId); // Preselect the ride ID if provided
        }
        model.addAttribute("reservationDto", reservationDto);

        // Fetch the authenticated user
        AppUser authenticatedUser = appUserService.getAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new IllegalStateException("No authenticated user found.");
        }

        // Fetch reservations for the current user
        if(user.getRole().equals("PASSENGER")){
            List<Reservation> userReservations = reservationsService.getReservationsByUser(authenticatedUser.getId_user());
            model.addAttribute("reservations", userReservations);
        }

        if(user.getRole().equals("CONDUCTOR")){
            List<Reservation> userReservations = reservationsService.getReservationsByConductor(authenticatedUser.getId_user());
            model.addAttribute("reservations", userReservations);
        }


        return "manageReservations";
    }

    // Create a new reservation
    @PostMapping("/create")
    public String createReservation(
            @Valid @ModelAttribute("reservationDto") ReservationDto reservationDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        // Fetch the authenticated user
        AppUser authenticatedUser = appUserService.getAuthenticatedUser();
        if (authenticatedUser == null || authenticatedUser.getId_user() == null) {
            redirectAttributes.addFlashAttribute("error", "No authenticated user found or user ID is null.");
            redirectAttributes.addFlashAttribute("reservationDto", reservationDto);
            return "redirect:/reservations/manage";
        }

        reservationDto.setUserId(authenticatedUser.getId_user());

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please fix the errors in the form.");
            redirectAttributes.addFlashAttribute("reservationDto", reservationDto);
            return "redirect:/reservations/manage";
        }

        try {
            reservationsService.createReservation(
                    reservationDto.getRideId(),
                    reservationDto.getUserId(),
                    reservationDto.getReservedPlaces()
            );
            redirectAttributes.addFlashAttribute("success", "Reservation created successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("reservationDto", reservationDto);
        }

        return "redirect:/reservations/manage";
    }


    // Delete a reservation
    @PostMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        reservationsService.cancelReservation(id);
        return "redirect:/reservations/manage";
    }

    // Redirect to the update page
    @GetMapping("/update/{id}")
    public String updateReservationForm(@PathVariable Long id, Model model) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        Reservation reservation = reservationsService.getReservationById(id);
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setRideId(reservation.getRide().getId_ride());
        reservationDto.setReservedPlaces(reservation.getReservedPlaces());

        model.addAttribute("reservationDto", reservationDto);
        model.addAttribute("reservationId", id);

        return "updateReservation";
    }

    // Handle reservation update
    @PostMapping("/update/{id}")
    public String updateReservation(
            @PathVariable Long id,
            @Valid @ModelAttribute("reservationDto") ReservationDto reservationDto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "updateReservation";
        }

        try {
            reservationsService.updateReservation(id, reservationDto.getReservedPlaces());
            model.addAttribute("success", "Reservation updated successfully!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/reservations/manage";
    }
}
