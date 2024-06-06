package com.hotelmanagement.kt3;

import java.util.Scanner;
import java.util.stream.Collectors;

import com.hotelmanagement.controller.DataController;
import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.model.Maid;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomStatus;

import java.util.ArrayList;
import java.util.List;

public class MaidMenu {
    
	public static void show() {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\nReceptionist Menu:");
            System.out.println("1. Display all rooms that need cleaning");
            System.out.println("2. Clean the room");
            System.out.println("3. Logout");

            System.out.print("Enter your choice: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    displayAllRoomsNeedCleaning();
                    break;
                case 2:
                    cleanRoom(scanner);
                    break;
                case 3:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 5);

        scanner.close();
    }
	
	
	public static void displayAllRoomsNeedCleaning() {
	    int maidId = DataController.getInstance().getCurrentUserId();
	    Maid maid = MaidController.getInstance().getMaidById(maidId);
	    if (maid == null) {
	        System.out.println("Maid not found.");
	        return;
	    }

	    ArrayList<Integer> roomsId = maid.getRoomsId();
	    System.out.println("Rooms IDs assigned to maid " + maid.getName() + ": " + roomsId);  // Display assigned room IDs

	    if (roomsId.isEmpty()) {
	        System.out.println("No rooms assigned for cleaning.");
	        return;
	    }

	    boolean foundCleaningRooms = false;
	    for (Integer roomId : roomsId) {
	        Room room = RoomController.getInstance().findRoomById(roomId);
	        if (room != null && room.getStatus() == RoomStatus.CLEANING_PROCESS) {
	            System.out.println("Room ID: " + room.getRoomId() + ", Room Number: " + room.getRoomNumber() + ", Status: " + room.getStatus());
	            foundCleaningRooms = true;
	        }
	    }

	    if (!foundCleaningRooms) {
	        System.out.println("No rooms currently need cleaning.");
	    }
	}


	
	public static void cleanRoom(Scanner scanner) {
        System.out.print("Enter the Room ID to clean: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();

        Room room = RoomController.getInstance().findRoomById(roomId);
        if (room != null && room.getStatus() == RoomStatus.CLEANING_PROCESS) {
            room.setStatus(RoomStatus.FREE);
            RoomController.getInstance().updateRoomStatus(room.getRoomId(), RoomStatus.FREE);
            System.out.println("Room ID: " + roomId + " has been cleaned and is now ready for new guests.");
        } else {
            System.out.println("Room not found or not in the correct status for cleaning.");
        }
    }
	
}
