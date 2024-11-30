package com.fst.ridebuddy.entities;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    private String first_name;
    private String last_name;
    @Column(unique = true, nullable = false)
    private String email;
    private String role;
    private String password;
    private boolean emailVerified = false;
    private String emailVerificationToken;

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


    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

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

    public float getAverage_rating() {
        if (reviewsReceived == null || reviewsReceived.isEmpty()) {
            return 0.0f;
        }
        return (float) reviewsReceived.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public float getTotalEarnings() {
        if (rides == null || rides.isEmpty()) {
            return 0.0f;
        }

        return (float) rides.stream()
                .filter(ride -> "over".equalsIgnoreCase(ride.getStatus()))
                .mapToDouble(ride -> ride.getReservations().size() * ride.getPricePerSeat().doubleValue())
                .sum();
    }

    public int getCompletedRidesAsConductor() {
        if (rides == null || rides.isEmpty()) {
            return 0;
        }
        return (int) rides.stream()
                .filter(ride -> "over".equalsIgnoreCase(ride.getStatus()))
                .count();
    }

    public int getCompletedRidesAsPassenger() {
        if (reservations == null || reservations.isEmpty()) {
            return 0;
        }

        return (int) reservations.stream()
                .filter(reservation -> "accepted".equalsIgnoreCase(reservation.getStatus()))
                .filter(reservation -> reservation.getRide() != null && "over".equalsIgnoreCase(reservation.getRide().getStatus()))
                .count();
    }


    @Override
    public String toString() {
        return "AppUser{" +
                "id_user=" + id_user +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", average_rating=" + getAverage_rating() +
                ", password='" + password + '\'' +
                ", profilePic=" + Arrays.toString(profilePic) +
                ", reservations=" + reservations +
                ", reviewsGiven=" + reviewsGiven +
                ", reviewsReceived=" + reviewsReceived +
                ", rides=" + rides +
                '}';
    }
}
