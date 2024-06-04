package com.hotelmanagement.kt3;

import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomStatus;
import com.hotelmanagement.model.RoomType;

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

        scanner.close();
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
        System.out.println("Reserving a room...");
        // Implement reservation logic here
    }

    private static void showAllReservations() {
        System.out.println("Showing all reservations...");
        // Implement logic to show all reservations here
    }

    private static void changeReservation() {
        System.out.println("Changing reservation...");
        // Implement logic to change a reservation here
    }

    private static void cancelReservation() {
        System.out.println("Cancelling reservation...");
        // Implement logic to cancel a reservation here
    }
}
