package com.hotelmanagement.controller;

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
	
}
