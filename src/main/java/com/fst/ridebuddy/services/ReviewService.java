package com.fst.ridebuddy.services;


import com.fst.ridebuddy.entities.Review;
import com.fst.ridebuddy.entities.Ride;
import com.fst.ridebuddy.repositories.ReservationsRepository;
import com.fst.ridebuddy.repositories.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class ReviewService {

    @Autowired
    private ReservationsRepository reservationRepository;
    @Autowired
    private ReviewsRepository reviewRepository;


    public void validateAndCreateReview(Review review) {
        Long rideId = review.getRide().getId_ride();
        Long reviewerId = review.getReviewer().getId_user();
        Long reviewedId = review.getReviewed().getId_user();
        String acceptedStatus = "accepted";

        // Check if both users (reviewer and reviewed) have an "accepted" reservation for the same ride
        boolean isReviewerInRide = false;
        boolean isReviewedInRide = false;
        if(Objects.equals(review.getReviewer().getRole(), "PASSENGER")) {
            isReviewerInRide = reservationRepository.existsReservation(rideId, reviewerId, acceptedStatus);
            isReviewedInRide = Objects.equals(review.getReviewed().getId_user(), review.getRide().getConductor().getId_user());
        }

        if(Objects.equals(review.getReviewer().getRole(), "CONDUCTOR")) {
            isReviewedInRide = reservationRepository.existsReservation(rideId, reviewedId, acceptedStatus);
            System.out.println(reviewedId);
            isReviewerInRide = Objects.equals(review.getReviewer().getId_user(), review.getRide().getConductor().getId_user());
        }
        System.out.println(isReviewerInRide);
        System.out.println(isReviewedInRide);
        if (!isReviewerInRide || !isReviewedInRide) {
            throw new IllegalArgumentException("Both the reviewer and reviewed must have accepted reservations for the same ride.");
        }

        reviewRepository.save(review);
    }
}
