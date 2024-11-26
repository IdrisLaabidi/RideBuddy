package com.fst.ridebuddy.entities;

import jakarta.persistence.*;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_res;

    private String status;
    private Integer reservedPlaces;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "id_ride")
    private Ride ride;

    // Getters and Setters

    public Long getId_res() {
        return id_res;
    }

    public void setId_res(Long id_res) {
        this.id_res = id_res;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getReservedPlaces() {
        return reservedPlaces;
    }

    public void setReservedPlaces(Integer reservedPlaces) {
        this.reservedPlaces = reservedPlaces;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }
}
