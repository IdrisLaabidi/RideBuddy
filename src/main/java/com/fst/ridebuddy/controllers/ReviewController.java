package com.fst.ridebuddy.controllers;


import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.entities.Review;
import com.fst.ridebuddy.entities.ReviewId;
import com.fst.ridebuddy.entities.Ride;
import com.fst.ridebuddy.models.ReviewDto;
import com.fst.ridebuddy.repositories.AppUsersRepository;
import com.fst.ridebuddy.services.AppUserService;
import com.fst.ridebuddy.services.ReservationsService;
import com.fst.ridebuddy.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.fst.ridebuddy.services.RideService;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReservationsService reservationsService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RideService rideService;

    @Autowired
    private AppUsersRepository appUsersRepository;

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/rate")
    public String rate(@ModelAttribute("reviewDto") ReviewDto reviewDto, Model model) {
        System.out.println(reviewDto);
        // Fetch entities using IDs from ReviewDto
        AppUser reviewer = appUsersRepository.findById(reviewDto.getReviewerId())
                .orElseThrow(() -> new IllegalArgumentException("Reviewer not found"));
        AppUser reviewed = appUsersRepository.findById(reviewDto.getReviewedId())
                .orElseThrow(() -> new IllegalArgumentException("Reviewed user not found"));
        Ride ride = rideService.getRideById(reviewDto.getRideId());


        ReviewId reviewId = new ReviewId();
        reviewId.setReviewerId(reviewer.getId_user());
        reviewId.setReviewedId(reviewed.getId_user());
        reviewId.setRideId(ride.getId_ride());


        Review review = new Review();
        review.setId(reviewId);
        review.setReviewer(reviewer);
        review.setReviewed(reviewed);
        review.setRide(ride);
        review.setComment(reviewDto.getComment());
        review.setPunctuality(reviewDto.getPunctuality());
        review.setDriving(reviewDto.getDriving());
        review.setFriendliness(reviewDto.getFriendliness());
        if(reviewDto.getFriendliness() != null && reviewDto.getDriving() != null && reviewDto.getPunctuality()!= null){
            review.setRating((float) (reviewDto.getFriendliness() + reviewDto.getDriving() + reviewDto.getPunctuality()) / 3);
        }else{
            review.setRating(reviewDto.getBehaviour());
        }
        review.setBehaviour(reviewDto.getBehaviour());
        System.out.println(reviewDto);
        // Save the review
        reviewService.validateAndCreateReview(review);

        model.addAttribute("successMessage", "Review submitted successfully!");
        AppUser user = appUserService.getAuthenticatedUser();
        if(user.getRole().equals("CONDUCTOR")){
            return "redirect:/rides/myRides";
        }
        return "redirect:/rides/ride-details/" + reviewDto.getRideId();
    }

    @GetMapping("/rate/{rideId}")
    public String rate(Model model,@PathVariable Long rideId) {
        AppUser user = appUserService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        Ride ride = rideService.getRideById(rideId);
        List<AppUser> usersInRide = reservationsService.findUsersInRide(rideId);
        model.addAttribute("ride",ride);
        model.addAttribute("usersInRide", usersInRide);
        return "rides/ratePassengers";
    }
}
