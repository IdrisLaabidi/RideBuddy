package com.fst.ridebuddy.models;

import jakarta.validation.constraints.NotNull;

public class ReservationDto {

    @NotNull(message = "Ride ID is required")
    private Long rideId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Number of reserved places is required")
    private Integer reservedPlaces;

    // Getters and setters
    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getReservedPlaces() {
        return reservedPlaces;
    }

    public void setReservedPlaces(Integer reservedPlaces) {
        this.reservedPlaces = reservedPlaces;
    }
}
