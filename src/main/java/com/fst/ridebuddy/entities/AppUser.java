package com.fst.ridebuddy.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    private String first_name;
    private String last_name;
    @Column(unique = true , nullable = false)
    private String email;
    private String role;
    private Double average_rating;
    private String password;

    @Lob
    private byte[] profilePic; // For storing profile pictures as binary data

    // A user can have multiple reservations
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    // A user can give multiple reviews
    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviewsGiven;

    // A user can receive multiple reviews
    @OneToMany(mappedBy = "reviewed", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviewsReceived;

    // A user (driver) can have multiple rides
    @OneToMany(mappedBy = "conductor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ride> rides;

    // Getters and Setters

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Double getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(Double average_rating) {
        this.average_rating = average_rating;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Review> getReviewsGiven() {
        return reviewsGiven;
    }

    public void setReviewsGiven(List<Review> reviewsGiven) {
        this.reviewsGiven = reviewsGiven;
    }

    public List<Review> getReviewsReceived() {
        return reviewsReceived;
    }

    public void setReviewsReceived(List<Review> reviewsReceived) {
        this.reviewsReceived = reviewsReceived;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }
}
