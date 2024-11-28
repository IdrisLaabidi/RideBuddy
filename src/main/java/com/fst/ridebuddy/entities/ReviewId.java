package com.fst.ridebuddy.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReviewId implements Serializable {

    private Long reviewerId;
    private Long reviewedId;
    private Long rideId;

    public ReviewId() {}

    public ReviewId(Long reviewerId, Long reviewedId, Long rideId) {
        this.reviewerId = reviewerId;
        this.reviewedId = reviewedId;
        this.rideId = rideId;
    }


    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Long getReviewedId() {
        return reviewedId;
    }

    public void setReviewedId(Long reviewedId) {
        this.reviewedId = reviewedId;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewId reviewId = (ReviewId) o;
        return Objects.equals(reviewerId, reviewId.reviewerId) &&
                Objects.equals(reviewedId, reviewId.reviewedId) &&
                Objects.equals(rideId, reviewId.rideId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewerId, reviewedId, rideId);
    }
}

