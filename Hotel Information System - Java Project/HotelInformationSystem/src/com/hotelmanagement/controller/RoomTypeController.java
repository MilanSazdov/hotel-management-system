package com.hotelmanagement.controller;

import java.util.ArrayList;

import com.hotelmanagement.model.RoomCategory;
import com.hotelmanagement.model.RoomType;

public class RoomTypeController {
	
	private static RoomTypeController instance;
	private ArrayList<RoomType> roomTypeList;
	
	private RoomTypeController() {
		roomTypeList = new ArrayList<>();
	}
	
	public static synchronized RoomTypeController getInstance() {
		if (instance == null) {
			instance = new RoomTypeController();
		}
		return instance;
	}
	
	public void addRoomType(RoomType roomType) {
		roomTypeList.add(roomType);
	}
	
	public void removeRoomType(RoomType roomType) {
		roomTypeList.remove(roomType);
	}
	
	public void displayAllRoomTypes() {
		for (RoomType roomType : roomTypeList) {
			System.out.println(roomType.toString());
		}
	}
	
	public void modifyRoomTypePrice(int id, double price) {
		for (RoomType roomType : roomTypeList) {
			if (roomType.getRoomTypeId() == id) {
				roomType.setPrice(price);
				System.out.println("Price modified successfully!");
			}
		}
	}
	
	public void modifyRoomTypeCategory(int id, RoomCategory category) {
		for (RoomType roomType : roomTypeList) {
			if (roomType.getRoomTypeId() == id) {
				roomType.setCategory(category);
				System.out.println("Category modified successfully!");
			}
		}
	}
	
	public ArrayList<RoomType> getRoomTypeList() {
		return roomTypeList;
	}
	
	public void setRoomTypeList(ArrayList<RoomType> roomTypeList) {
		this.roomTypeList = roomTypeList;
	}
}
