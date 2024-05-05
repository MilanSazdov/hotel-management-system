package com.hotelmanagement.controller;

import java.time.LocalDate;
import java.util.ArrayList;

import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomCategory;
import com.hotelmanagement.model.RoomType;

public class RoomController {
	
	private static RoomController instance;
	private ArrayList<Room> roomList;

	private RoomController() {
		roomList = new ArrayList<>();
	}

	public static synchronized RoomController getInstance() {
		if (instance == null) {
			instance = new RoomController();
		}
		return instance;
	}

	public void addRoom(Room room) {
		roomList.add(room);
	}

	public void removeRoom(Room room) {
		roomList.remove(room);
	}

	public void displayAllRooms() {
		for (Room room : roomList) {
			System.out.println(room.toString());
		}
	}

	public ArrayList<Room> getRoomList() {
		return roomList;
	}

	public void modifyRoomCategory(int id, RoomCategory category) {
		for (Room room : roomList) {
			if (room.getRoomId() == id) {
				RoomTypeController.getInstance().modifyRoomTypeCategory(room.getRoomType().getRoomTypeId(), category);
			}
		}
	}
	
	public void modifyRoomType(int id, RoomType roomType) {
		for (Room room : roomList) {
			if (room.getRoomId() == id) {
				room.setRoomType(roomType);
			}
		}
	}
	
	public boolean isRoomFreeByTimePeriod(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        return room.isRoomFreeByTimePeriod(checkInDate, checkOutDate);
    }
	
	public void displayFreeRoomTypesByTimePeriod(LocalDate checkInDate, LocalDate checkOutDate) {
        System.out.println("Free Room Types from " + checkInDate + " to " + checkOutDate + ":");
        for (Room room : roomList) {
            if (isRoomFreeByTimePeriod(room, checkInDate, checkOutDate)) {
                System.out.println(room.getRoomType().toString());
            }
        }
    }
	
	public void displayRoomById(int id) {
		for (Room room : roomList) {
			if (room.getRoomId() == id) {
				System.out.println(room.toString());
			}
		}
	}
	
	public void searchRoomById(int id) {
		for (Room room : roomList) {
			if (room.getRoomId() == id) {
				System.out.println(room.toString());
			}
		}
	}
}
