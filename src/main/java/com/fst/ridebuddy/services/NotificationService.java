package com.fst.ridebuddy.services;

import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.entities.Ride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private EmailService emailService;

    // Notify user of acceptance in a ride
    public void notifyAcceptedInRide(AppUser user, Ride ride) {
        String content = String.format("Hi %s,\n\nYou have been accepted for the ride from %s to %s. Enjoy your journey!\n\nBest regards,\nRideBuddy Team",
                user.getFirst_name(), ride.getDepartureLocation(), ride.getDestination());
        emailService.sendEmail(user.getEmail(), "Ride Accepted", content);
    }

    // Notify user of rejection from a ride
    public void notifyRejectedInRide(AppUser user, Ride ride) {
        String content = String.format("Hi %s,\n\nUnfortunately, you have been rejected for the ride from %s to %s. We encourage you to explore other ride options.\n\nBest regards,\nRideBuddy Team",
                user.getFirst_name(), ride.getDepartureLocation(), ride.getDestination());
        emailService.sendEmail(user.getEmail(), "Ride Rejected", content);
    }

    // Notify all users in a ride when it is canceled
    public void notifyRideCanceled(List<AppUser> users, Ride ride) {
        String content = String.format("Hi,\n\nWe regret to inform you that the ride from %s to %s scheduled at %s has been canceled. We apologize for the inconvenience.\n\nBest regards,\nRideBuddy Team",
                ride.getDepartureLocation(), ride.getDestination(), ride.getDepartureTime());
        for (AppUser user : users) {
            emailService.sendEmail(user.getEmail(), "Ride Canceled", content);
        }
    }

    // Notify all users in a ride when it is marked as "over"
    public void notifyRideOver(List<AppUser> users, Ride ride) {
        String content = String.format("Hi,\n\nWe hope you had a pleasant journey! The ride from %s to %s has been marked as completed.\n\nBest regards,\nRideBuddy Team",
                ride.getDepartureLocation(), ride.getDestination());
        for (AppUser user : users) {
            emailService.sendEmail(user.getEmail(), "Ride Completed", content);
        }
    }

    // Notify all users in a ride when it is edited by the driver
    public void notifyRideEdited(List<AppUser> users, Ride ride) {
        String content = String.format("Hi,\n\nThe details for your ride from %s to %s scheduled at %s have been updated. Please review the updated ride details in the app.\n\nBest regards,\nRideBuddy Team",
                ride.getDepartureLocation(), ride.getDestination(), ride.getDepartureTime());
        for (AppUser user : users) {
            emailService.sendEmail(user.getEmail(), "Ride Details Updated", content);
        }
    }
}

