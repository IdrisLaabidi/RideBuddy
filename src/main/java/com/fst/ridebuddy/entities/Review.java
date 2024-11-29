package com.fst.ridebuddy.entities;

import jakarta.persistence.*;

@Entity
public class Review {

    @EmbeddedId
    private ReviewId id; // Composite key

    private float rating;
    private String comment;
    private Integer punctuality;
    private Integer driving;
    private Integer friendliness;
    private Integer behaviour;

    @ManyToOne
    @MapsId("reviewerId")
    @JoinColumn(name = "reviewer_id")
    private AppUser reviewer;

    @ManyToOne
    @MapsId("reviewedId")
    @JoinColumn(name = "reviewed_id")
    private AppUser reviewed;

    @ManyToOne
    @MapsId("rideId")
    @JoinColumn(name = "ride_id")
    private Ride ride;

    // Getters and Setters
    public ReviewId getId() {
        return id;
    }

    public void setId(ReviewId id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getPunctuality() {
        return punctuality;
    }

    public void setPunctuality(Integer punctuality) {
        this.punctuality = punctuality;
    }

    public Integer getDriving() {
        return driving;
    }

    public void setDriving(Integer driving) {
        this.driving = driving;
    }

    public Integer getFriendliness() {
        return friendliness;
    }

    public void setFriendliness(Integer friendliness) {
        this.friendliness = friendliness;
    }

    public Integer getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(Integer behaviour) {
        this.behaviour = behaviour;
    }

    public AppUser getReviewer() {
        return reviewer;
    }

    public void setReviewer(AppUser reviewer) {
        this.reviewer = reviewer;
    }

    public AppUser getReviewed() {
        return reviewed;
    }

    public void setReviewed(AppUser reviewed) {
        this.reviewed = reviewed;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }
}
