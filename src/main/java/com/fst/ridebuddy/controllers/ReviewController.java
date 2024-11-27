package com.fst.ridebuddy.controllers;


import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.entities.Review;
import com.fst.ridebuddy.entities.ReviewId;
import com.fst.ridebuddy.entities.Ride;
import com.fst.ridebuddy.models.ReviewDto;
import com.fst.ridebuddy.repositories.AppUsersRepository;
import com.fst.ridebuddy.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fst.ridebuddy.services.RideService;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RideService rideService;

    @Autowired
    private AppUsersRepository appUsersRepository;

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

        // Save the review
        reviewService.validateAndCreateReview(review);

        model.addAttribute("successMessage", "Review submitted successfully!");

        return "redirect:/rides/ride-details/" + reviewDto.getRideId();
    }
}
