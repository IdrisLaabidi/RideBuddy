package com.fst.ridebuddy.repositories;

import com.fst.ridebuddy.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewsRepository extends JpaRepository<Review, Long> {
}
