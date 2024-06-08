package com.hotelmanagement.kt3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.model.AdditionalServices;
import com.hotelmanagement.model.Gender;
import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.Maid;
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
            System.out.println("5. Register a new guest");
            System.out.println("6. View daily hotel activities");
            System.out.println("7. Logout");

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
					registerGuest();
					break;
                case 6:
                    displayDailyActivities(scanner);
                    break;
                case 7:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 5);

        // scanner.close();
    }
    
    
    private static void displayDailyActivities(Scanner scanner) {
        System.out.println("Enter the date for daily activities (YYYY-MM-DD):");
        String inputDate = scanner.nextLine();
        LocalDate selectedDate = LocalDate.parse(inputDate);

        ArrayList<Reservation> reservations = ReservationController.getInstance().getAllReservations();
        ArrayList<Reservation> arrivals = new ArrayList<>();
        ArrayList<Reservation> departures = new ArrayList<>();
        int occupiedRooms = 0;

        for (Reservation res : reservations) {
            if (res.getCheckInDate().isEqual(selectedDate)) {
                arrivals.add(res);
            }
            if (res.getCheckOutDate().isEqual(selectedDate)) {
                departures.add(res);
            }
            if (!res.getCheckOutDate().isBefore(selectedDate) && !res.getCheckInDate().isAfter(selectedDate)) {
                occupiedRooms++;
            }
        }

        int totalRooms = RoomController.getInstance().getAllRooms().size();

        System.out.println("\nDaily Hotel Activities for " + selectedDate + ":");
        System.out.println("Arrivals:");
        for (Reservation arrival : arrivals) {
            System.out.println("Reservation ID: " + arrival.getReservationId() + ", Guest Name: " + GuestController.getInstance().getGuestById(arrival.getGuestId()).getName());
        }

        System.out.println("Departures:");
        for (Reservation departure : departures) {
            System.out.println("Reservation ID: " + departure.getReservationId() + ", Guest Name: " + GuestController.getInstance().getGuestById(departure.getGuestId()).getName());
        }

        System.out.println("Current Occupancy: " + occupiedRooms + "/" + totalRooms);
    }
    
    
    private static void registerGuest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Register a new guest.");
        
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Enter gender (MALE, FEMALE, OTHER): ");
        String gender = scanner.nextLine();
        
        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();
        LocalDate dateOfBirth = LocalDate.parse(dob);
        
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        
        
        System.out.print("Enter email (to be used as username): ");
        String email = scanner.nextLine();
        
        System.out.print("Enter passport number (to be used as password): ");
        String passportNumber = scanner.nextLine();

        
        Guest newGuest = new Guest(firstName, lastName, Gender.valueOf(gender.toUpperCase()), dateOfBirth, phoneNumber, email, passportNumber);
       
        GuestController.getInstance().addGuest(newGuest);
        
        int nextId = GuestController.getInstance().getNextId();
        
        newGuest.setGuestId(nextId);
        GuestController.getInstance().appendGuestToFile(newGuest);

        
        System.out.println("Guest registered successfully. Username: " + email + ", Password: " + passportNumber);
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
            reservation.setStatus(ReservationStatus.REJECTED);
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
        
        // Add check-in and check-out dates to the room
        room.addCheckInDate(reservation.getCheckInDate());
        room.addCheckOutDate(reservation.getCheckOutDate());

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
        reservation.setStatus(ReservationStatus.CHECKED_IN);
    }


    private static void checkOutGuest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the guest's email for check-out:");
        String guestEmail = scanner.nextLine();

        Guest guest = GuestController.getInstance().getGuestByEmail(guestEmail);
        if (guest == null) {
            System.out.println("No guest found with that email.");
            return;
        }

        Reservation activeReservation = guest.getReservations().stream()
            .filter(res -> res.getStatus() == ReservationStatus.CHECKED_IN)
            .findFirst()
            .orElse(null);

        if (activeReservation == null) {
            System.out.println("No active reservation found for check-out.");
            return;
        }

        Room room = activeReservation.getRoom();
        if (room == null) {
            System.out.println("No room associated with this reservation.");
            return;
        }

        // Update the reservation status to CHECKED_OUT
        activeReservation.setStatus(ReservationStatus.CHECKED_OUT);
        ReservationController.getInstance().updateReservation(activeReservation); // Persist changes

        // Update room status to CLEANING
        room.setStatus(RoomStatus.CLEANING_PROCESS);
        RoomController.getInstance().updateRoomAttributes(room);

        // Assign the room to the maid with the least number of assigned rooms
        Maid assignedMaid = MaidController.getInstance().getMaidList().stream()
            .min(Comparator.comparingInt(maid -> maid.getRoomsId().size()))
            .orElse(null);

        if (assignedMaid != null) {
            assignedMaid.addRoomId(room.getRoomId());
            System.out.println(assignedMaid.getRoomsId());
            MaidController.getInstance().updateMaid(assignedMaid);
            System.out.println("Room " + room.getRoomNumber() + " assigned to Maid " + assignedMaid.getName() + " for cleaning.");
        } else {
            System.out.println("No maid available to assign for cleaning.");
        }

        System.out.println("Check-out completed for " + guest.getName() + ".");
    }







}
