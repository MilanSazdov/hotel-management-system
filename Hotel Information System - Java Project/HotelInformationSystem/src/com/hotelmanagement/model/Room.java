package com.hotelmanagement.model;

public class Room {
	
	private int roomNumber;
	private RoomType roomType;
	private RoomStatus status;
	private String roomDescription;
	private static int nextRoomId = 1;
	private int roomId;
	
	
	public Room(int roomNumber, RoomType roomType, RoomStatus status, String roomDescription) {
		this.roomNumber = roomNumber;
		this.roomType = roomType;
		this.status = status;
		this.roomDescription = roomDescription;
		this.roomId = nextRoomId++;
	}
	
	public int getRoomId() {
		return this.roomId;
	}
	
	public int getRoomNumber() {
		return this.roomNumber;
	}
	
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	
	public RoomType getRoomType() {
		return this.roomType;
	}
	
	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}
	
	public RoomStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(RoomStatus status) {
		this.status = status;
	}
	
	public String getRoomDescription() {
		return this.roomDescription;
	}
	
	public void setRoomDescription(String roomDescription) {
		this.roomDescription = roomDescription;
	}
	
	@Override
	public String toString() {
		return "Room Id: " + this.roomId + " Room Number: " + this.roomNumber + ", Room Type: " + this.roomType + ", Room Status: " + this.status
				+ ", Room Description: " + this.roomDescription;
	}
}
