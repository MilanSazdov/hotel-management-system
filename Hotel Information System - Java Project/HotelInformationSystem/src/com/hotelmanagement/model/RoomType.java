package com.hotelmanagement.model;

public class RoomType {
	
	private int numberOfBeds;
	private RoomCategory category;
	private double price;
	private static int nextRoomTypeId = 1;
	private int roomTypeId;
	
	public RoomType(int numberOfBeds, RoomCategory category, double price) {
		this.numberOfBeds = numberOfBeds;
		this.category = category;
		this.price = price;
		this.roomTypeId = nextRoomTypeId++;
	}
	
	public RoomType(int id, int numberOfBeds, RoomCategory category, double price) {
		this.numberOfBeds = numberOfBeds;
		this.category = category;
		this.price = price;
		this.roomTypeId = id;
	}
	
	public int getRoomTypeId() {
		return this.roomTypeId;
	}
	
	public int getNumberOfBeds() {
		return this.numberOfBeds;
	}
	
	public void setNumberOfBeds(int numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}
	
	public RoomCategory getCategory() {
		return this.category;
	}
	
	public void setCategory(RoomCategory category) {
		this.category = category;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String toCSVString() {
        return roomTypeId + "," + numberOfBeds + "," + category.name() + "," + price;
    }
	
	@Override
	public String toString() {
		return "Room Type: " + this.category + ", Number of Beds: " + this.numberOfBeds + ", Price: " + this.price; 
	}
}
