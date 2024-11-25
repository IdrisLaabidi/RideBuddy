package com.fst.ridebuddy.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;  // Use LocalDateTime for departureTime
import java.util.List;

@Entity
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ride;

    private String departureLocation;
    private LocalDateTime departureTime;  // Combined DateTime field
    private String destination;
    private Integer availablePlaces;
    private BigDecimal pricePerSeat;
    private String status; // "over" or "in-progress"
    private String comments;
    private String startCoordinate;
    private String endCoordinate;
    private String statGov;
    private String endGov;

    @ManyToOne
    @JoinColumn(name = "conductor_id", nullable = false)
    private AppUser conductor;

    @OneToMany(mappedBy = "ride", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "ride", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    // Getters and Setters

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

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getAvailablePlaces() {
        return availablePlaces;
    }

    public void setAvailablePlaces(Integer availablePlaces) {
        this.availablePlaces = availablePlaces;
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

    public AppUser getConductor() {
        return conductor;
    }

    public void setConductor(AppUser conductor) {
        this.conductor = conductor;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public BigDecimal getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(BigDecimal pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartCoordinate() {
        return startCoordinate;
    }

    public void setStartCoordinate(String startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    public String getEndCoordinate() {
        return endCoordinate;
    }

    public void setEndCoordinate(String endCoordinate) {
        this.endCoordinate = endCoordinate;
    }

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
}
