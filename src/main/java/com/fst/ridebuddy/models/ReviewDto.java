package com.fst.ridebuddy.models;


public class ReviewDto {
    private Long rideId;
    private Long reviewerId;
    private Long reviewedId;
    private String comment;
    private Integer punctuality;
    private Integer driving;
    private Integer friendliness;
    private Integer behaviour;
    private Integer rating;

    // Getters and Setters


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

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "ReviewDto{" +
                "rideId=" + rideId +
                ", reviewerId=" + reviewerId +
                ", reviewedId=" + reviewedId +
                ", comment='" + comment + '\'' +
                ", punctuality=" + punctuality +
                ", driving=" + driving +
                ", friendliness=" + friendliness +
                ", behaviour=" + behaviour +
                ", rating=" + rating +
                '}';
    }
}
