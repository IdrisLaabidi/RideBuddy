package com.fst.ridebuddy.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class RideDTO {
    private Long id_ride;
    private String departureLocation;
    private LocalDate departureDate;  // Separate field for Date
    private LocalTime departureTime;  // Separate field for Time
    private String destination;
    private Integer availablePlaces;
    private BigDecimal pricePerSeat;
    private String status;
    private String comments;
    private String statGov;
    private String endGov;

    // Getters and Setters


    public String getStatGov() {
        return statGov;
    }

    public void setStatGov(String statGov) {
        this.statGov = statGov;
    }

    public String getEndGov() {
        return endGov;
    }

    public void setEndGov(String endGov) {
        this.endGov = endGov;
    }

    public Long getId_ride() {
        return id_ride;
    }

    public void setId_ride(Long id_ride) {
        this.id_ride = id_ride;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getAvailablePlaces() {
        return availablePlaces;
    }

    public void setAvailablePlaces(Integer availablePlaces) {
        this.availablePlaces = availablePlaces;
    }

    public BigDecimal getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(BigDecimal pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
