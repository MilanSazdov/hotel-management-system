package com.hotelmanagement.kt3;

import java.util.ArrayList;
import java.util.Scanner;

import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;

public class ReceptionistMenu {

    public static void show() {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\nReceptionist Menu:");
            System.out.println("1. Display all reservations in the system");
            System.out.println("2. Check-in guest");
            System.out.println("3. Check-out guest");
            System.out.println("4. Confirm reservations");
            System.out.println("5. Logout");

            System.out.print("Enter your choice: ");
            option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (option) {
                case 1:
                    displayAllReservations();
                    break;
                case 2:
                    checkInGuest();
                    break;
                case 3:
                    checkOutGuest();
                    break;
                case 4:
                    confirmReservations(scanner);
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return; // Logout and exit the menu loop
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 5);

        scanner.close();
    }

    private static void displayAllReservations() {
        System.out.println("All reservations in the system:");
        ArrayList<Reservation> reservations = ReservationController.getInstance().getAllReservations();
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
        }
    }

    private static void confirmReservations(Scanner scanner) {
        System.out.println("Reservations awaiting confirmation:");
        ArrayList<Reservation> reservations = ReservationController.getInstance().getReservationsByStatus(ReservationStatus.WAITING);
        if (reservations.isEmpty()) {
            System.out.println("No reservations waiting for confirmation.");
            return;
        }
        for (Reservation res : reservations) {
            System.out.println(res);
        }

        System.out.print("Enter the reservation ID to confirm or reject: ");
        int resId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Reservation reservation = ReservationController.getInstance().findReservationById(resId);

        if (reservation == null) {
            System.out.println("Reservation not found.");
            return;
        }

        System.out.println("Do you want to confirm (1) or reject (2) this reservation?");
        int confirm = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (confirm == 1) {
            reservation.setStatus(ReservationStatus.CONFIRMED);
            System.out.println("Reservation confirmed.");
        } else if (confirm == 2) {
            reservation.setStatus(ReservationStatus.REJECTED);
            System.out.println("Reservation rejected.");
        } else {
            System.out.println("Invalid option.");
        }
    }

    
    private static void checkInGuest() {
        
    }

    private static void checkOutGuest() {
        System.out.println("Check-out guest functionality not implemented yet.");
    }
}
