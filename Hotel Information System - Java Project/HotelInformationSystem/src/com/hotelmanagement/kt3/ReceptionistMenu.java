package com.hotelmanagement.kt3;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.model.AdditionalServices;
import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomStatus;
import com.hotelmanagement.model.RoomType;

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
                     // checkOutGuest();
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
        scanner.nextLine();
        Reservation reservation = ReservationController.getInstance().findReservationById(resId);

        if (reservation == null) {
            System.out.println("Reservation not found.");
            return;
        }

        System.out.println("Do you want to confirm (1) or reject (2) this reservation?");
        int confirm = scanner.nextInt();
        scanner.nextLine();

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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the guest's email for check-in:");
        String guestEmail = scanner.nextLine();

        Guest guest = GuestController.getInstance().getGuestByEmail(guestEmail);
        if (guest == null) {
            System.out.println("No guest found with that email.");
            return;
        }

        ArrayList<Reservation> confirmedReservations = ReservationController.getInstance().getReservationsByGuestAndStatus(guest.getGuestId(), ReservationStatus.CONFIRMED).stream()
            .filter(res -> res.getRoom() == null)
            .collect(Collectors.toCollection(ArrayList::new));

        if (confirmedReservations.isEmpty()) {
            System.out.println("No confirmed reservations without a room found for guest with email: " + guestEmail);
            return;
        }

        System.out.println("Confirmed reservations without a room for " + guest.getName() + ":");
        confirmedReservations.forEach(res -> System.out.println("Reservation ID: " + res.getReservationId() + " - Room Type: " + res.getRoomType().getCategory() + " from " + res.getCheckInDate() + " to " + res.getCheckOutDate()));

        System.out.println("Enter the reservation ID to check in:");
        int reservationId = scanner.nextInt();
        scanner.nextLine();
        Reservation reservation = confirmedReservations.stream()
            .filter(res -> res.getReservationId() == reservationId)
            .findFirst()
            .orElse(null);

        if (reservation == null) {
            System.out.println("Reservation not found, not confirmed, or already has a room assigned.");
            return;
        }

        ArrayList<Room> availableRooms = RoomController.getInstance().getAllRooms().stream()
            .filter(room -> room.getRoomType().equals(reservation.getRoomType()) && room.isRoomFreeByTimePeriod(reservation.getCheckInDate(), reservation.getCheckOutDate()))
            .collect(Collectors.toCollection(ArrayList::new));

        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms of the requested type: " + reservation.getRoomType().getCategory() + " for the selected period.");
            return;
        }

        System.out.println("Available rooms of type " + reservation.getRoomType().getCategory() + ":");
        availableRooms.forEach(room -> System.out.println("Room ID: " + room.getRoomId() + ", Room Number: " + room.getRoomNumber()));

        System.out.println("Enter the Room ID to assign to this reservation:");
        int roomId = scanner.nextInt();
        scanner.nextLine();
        Room room = RoomController.getInstance().findRoomById(roomId);
        if (room == null || !room.isRoomFreeByTimePeriod(reservation.getCheckInDate(), reservation.getCheckOutDate())) {
            System.out.println("Invalid room selection or room is not free.");
            return;
        }

        reservation.setRoom(room);
        room.setStatus(RoomStatus.OCCUPIED);

        ArrayList<AdditionalServices> newServices = new ArrayList<>(reservation.getAdditionalServices()); // Copy existing services
        AdditionalServicesController.getInstance().getAdditionalServicesList().stream()
            .filter(service -> !reservation.getAdditionalServices().contains(service))
            .forEach(service -> {
                System.out.println(service.getServiceId() + ": " + service.getServiceName());
            });

        System.out.println("Would you like any additional services? Enter -1 if no additional services are needed.");
        String input = scanner.nextLine();
        if (!input.equals("-1")) {
            String[] serviceIndices = input.split(",");
            for (String index : serviceIndices) {
                try {
                    int idx = Integer.parseInt(index.trim());
                    AdditionalServices service = AdditionalServicesController.getInstance().getAdditionalServicesList().get(idx - 1);
                    if (!newServices.contains(service)) {
                        newServices.add(service);
                        System.out.println(service.getServiceName() + " added.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid service index or input: " + index);
                }
            }
        }

        reservation.setAdditionalServices(newServices);
        System.out.println("Check-in completed for " + guest.getName() + " in Room: " + room.getRoomNumber());
        ReservationController.getInstance().updateReservation(reservation);
    }









}
