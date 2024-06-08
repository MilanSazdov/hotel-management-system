package com.hotelmanagement.kt3;

import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.controller.DataController;
import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.controller.PriceListController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.model.AdditionalServices;
import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.PriceList;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomStatus;
import com.hotelmanagement.model.RoomType;
import com.hotelmanagement.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GuestMenu {

    public static void show() {
        Scanner scanner = new Scanner(System.in);
        int option;
        

        do {
            System.out.println("\nGuest Menu:");
            System.out.println("1. Display all rooms");
            System.out.println("2. Reserve room");
            System.out.println("3. Show all reservations");
            System.out.println("4. Change reservation");
            System.out.println("5. Cancel reservation");
            System.out.println("6. LogOut");

            System.out.print("Enter your choice: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    displayAllRooms();
                    break;
                case 2:
                    reserveRoom();
                    break;
                case 3:
                    showAllReservations();
                    break;
                case 4:
                    changeReservation();
                    break;
                case 5:
                    cancelReservation();
                    break;
                case 6:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 6);

    }

    private static void displayAllRooms() {
        RoomController roomController = RoomController.getInstance();
        Map<RoomType, Integer> availableRoomTypes = new HashMap<>();

        for (Room room : roomController.getRoomList()) {
            if (room.getStatus() == RoomStatus.FREE) {
                RoomType roomType = room.getRoomType();
                availableRoomTypes.put(roomType, availableRoomTypes.getOrDefault(roomType, 0) + 1);
            }
        }

        if (availableRoomTypes.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            for (Map.Entry<RoomType, Integer> entry : availableRoomTypes.entrySet()) {
                System.out.println("There are " + entry.getValue() + " available rooms of type " + entry.getKey().getCategory());
            }
        }
    }

    private static void reserveRoom() {
        Scanner scanner = new Scanner(System.in);

        // Unos datuma za rezervaciju
        System.out.println("Enter check-in date (YYYY-MM-DD): ");
        String checkInDateString = scanner.nextLine();
        LocalDate checkInDate = LocalDate.parse(checkInDateString);

        System.out.println("Enter check-out date (YYYY-MM-DD): ");
        String checkOutDateString = scanner.nextLine();
        LocalDate checkOutDate = LocalDate.parse(checkOutDateString);

        // Find applicable PriceList for the dates
        PriceList applicablePriceList = PriceListController.getInstance().getApplicablePriceListForDate(checkInDate);
        if (applicablePriceList == null) {
            System.out.println("No applicable price list found for the selected dates.");
            return;
        }

        // Prikazivanje svih dostupnih tipova soba u datom periodu
        RoomController roomController = RoomController.getInstance();
        Map<RoomType, Integer> availableRoomTypes = new HashMap<>();

        for (Room room : roomController.getRoomList()) {
            if (roomController.isRoomFreeByTimePeriod(room, checkInDate, checkOutDate)) {
                RoomType roomType = room.getRoomType();
                availableRoomTypes.put(roomType, availableRoomTypes.getOrDefault(roomType, 0) + 1);
            }
        }

        if (availableRoomTypes.isEmpty()) {
            System.out.println("No rooms available for the selected dates.");
            return;
        } else {
            System.out.println("Available rooms for the selected dates:");
            for (Map.Entry<RoomType, Integer> entry : availableRoomTypes.entrySet()) {
                double price = applicablePriceList.getPriceForRoomTypeId(entry.getKey().getRoomTypeId());
                System.out.println("There are " + entry.getValue() + " available rooms of type " + entry.getKey().getCategory() + " at $" + price + " per night.");
            }
        }

        // Unos odabira sobe
        System.out.println("Enter the number of the room you want to reserve: ");
        int roomIndex = scanner.nextInt();
        scanner.nextLine();
        
        if (roomIndex < 1 || roomIndex > availableRoomTypes.size()) {
            System.out.println("Invalid room number. Please try again.");
            return; // Exit if invalid index
        }
        
        RoomType selectedRoomType = (RoomType) availableRoomTypes.keySet().toArray()[roomIndex - 1];
        System.out.println(selectedRoomType.getCategory() + " room selected.");

        // Adding logic for additional services
        System.out.println("Would you like any additional services? Enter -1 if no additional services are needed.");
        AdditionalServicesController additionalServicesController = AdditionalServicesController.getInstance();
        additionalServicesController.displayAllAdditionalServices();

        ArrayList<AdditionalServices> selectedServices = new ArrayList<>();
        System.out.println("Enter the indices of the desired services separated by commas, or -1 if none:");
        String input = scanner.nextLine();
        if (!input.equals("-1")) {
            String[] serviceIndices = input.split(",");
            for (String index : serviceIndices) {
                try {
                    int idx = Integer.parseInt(index.trim());
                    if (idx >= 1 && idx <= additionalServicesController.getAdditionalServicesList().size()) {
                        AdditionalServices service = additionalServicesController.getAdditionalServicesList().get(idx - 1);
                        double servicePrice = applicablePriceList.getPriceForAdditionalServiceId(service.getServiceId());
                        selectedServices.add(service);
                        System.out.println(service.getServiceName() + " selected at $" + servicePrice);
                    } else {
                        System.out.println("Invalid service index: " + idx);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input: " + index);
                }
            }
        } else {
            System.out.println("No additional services selected.");
        }
        int guest_id = DataController.getInstance().getCurrentUserId();
        // Reservation creation (assuming other necessary details are correct and available)
        Reservation request = new Reservation(null, selectedRoomType, checkInDate, checkOutDate, ReservationStatus.WAITING, guest_id, selectedServices);
        GuestController gc = GuestController.getInstance();
        gc.getGuestById(guest_id).requestReservation(request);
        System.out.println("Reservation request sent. Please wait for confirmation.");
        ReservationController.getInstance().addReservation(request);
    
    }

    private static void showAllReservations() {
        System.out.println("Showing all reservations...");
        int guestId = DataController.getInstance().getCurrentUserId();
        Guest guest = GuestController.getInstance().getGuestById(guestId);

        if (guest == null) {
            System.out.println("No guest found with ID: " + guestId);
            return;
        }

        ArrayList<Reservation> reservations = guest.getReservations();

        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found for guest with ID: " + guestId);
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }



    private static void changeReservation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the reservation ID to modify:");
        int reservationId = scanner.nextInt();
        scanner.nextLine();  // Consume the newline

        // Fetch the reservation from the ReservationController
        Reservation reservation = ReservationController.getInstance().findReservationById(reservationId);
        if (reservation == null) {
            System.out.println("Reservation not found.");
            return;
        }

        // Example of changing dates, add similar blocks for changing room type or services
        System.out.println("Current check-in date: " + reservation.getCheckInDate());
        System.out.println("Enter new check-in date (YYYY-MM-DD):");
        LocalDate newCheckInDate = LocalDate.parse(scanner.nextLine());
        System.out.println("Current check-out date: " + reservation.getCheckOutDate());
        System.out.println("Enter new check-out date (YYYY-MM-DD):");
        LocalDate newCheckOutDate = LocalDate.parse(scanner.nextLine());

        // Update the reservation
        reservation.setCheckInDate(newCheckInDate);
        reservation.setCheckOutDate(newCheckOutDate);

        // Confirm the changes
        System.out.println("Reservation updated successfully.");
        System.out.println(reservation);  // Assuming Reservation has a suitable toString method
    }


    private static void cancelReservation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the reservation ID to cancel:");
        int reservationId = scanner.nextInt();
        scanner.nextLine();  // Consume the newline

        boolean isCancelled = ReservationController.getInstance().cancelReservation(reservationId);
        if (isCancelled) {
            System.out.println("Reservation status changed to CANCELLED successfully.");
        } else {
            System.out.println("Failed to change reservation status. It may not exist, be already completed, or not in a cancellable state.");
        }
    }


}
