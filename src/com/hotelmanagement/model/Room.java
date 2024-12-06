package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Room {
	
	private int roomNumber;
	private RoomType roomType;
	private RoomStatus status;
	private String roomDescription;
	private static int nextRoomId = 1;
	private int roomId;
	private ArrayList<LocalDate> checkInDates;
	private ArrayList<LocalDate> checkOutDates;
	
	public Room(int id, int roomNumber, RoomType roomType, RoomStatus status, String roomDescription, ArrayList<LocalDate> checkInDates, ArrayList<LocalDate> checkOutDates) {
		this.roomNumber = roomNumber;
		this.roomType = roomType;
		this.status = status;
		this.roomDescription = roomDescription;
		this.roomId = id++;
		this.nextRoomId = id;
		this.checkInDates = checkInDates;
		this.checkOutDates = checkOutDates;
	}
	
	public ArrayList<LocalDate> getCheckInDates() {
		return this.checkInDates;
	}
	
	public void setCheckInDates(ArrayList<LocalDate> checkInDates) {
		this.checkInDates = checkInDates;
	}
	
	public ArrayList<LocalDate> getCheckOutDates() {
		return this.checkOutDates;
	}
	
	public void setCheckOutDates(ArrayList<LocalDate> checkOutDates) {
		this.checkOutDates = checkOutDates;
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
	
	public void addCheckInDate(LocalDate checkInDate) {
		this.checkInDates.add(checkInDate);
	}
	
	public void removeCheckInDate(LocalDate checkInDate) {
		this.checkInDates.remove(checkInDate);
	}
	
	public void addCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDates.add(checkOutDate);
	}
	
	public void removeCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDates.remove(checkOutDate);
	}
	
	public boolean isRoomFreeByTimePeriod(LocalDate checkInDate, LocalDate checkOutDate) {
        for (int i = 0; i < checkInDates.size(); i++) {
            LocalDate existingCheckIn = checkInDates.get(i);
            LocalDate existingCheckOut = checkOutDates.get(i);
            
            // Provera da li se period preklapa
            if (!(checkOutDate.isBefore(existingCheckIn) || checkInDate.isAfter(existingCheckOut))) {
                return false; // Soba je zauzeta u ovom periodu
            }
        }
        return true; // Soba je slobodna u datom periodu
    }
	
	public String toCSVString() {
        return roomId + "," + roomNumber + "," + roomType.getRoomTypeId() + "," + status + "," + roomDescription + "," 
               + formatDates(checkInDates) + "," + formatDates(checkOutDates);
    }

	// Helper method to format dates as a semicolon-separated string
	private String formatDates(ArrayList<LocalDate> dates) {
	    StringBuilder sb = new StringBuilder();
	    for (LocalDate date : dates) {
	        if (sb.length() > 0) {
	            sb.append(";");
	        }
	        sb.append(date.toString());
	    }
	    return sb.toString();
	}
	
	@Override
	public String toString() {
		return "Room Id: " + this.roomId + " Room Number: " + this.roomNumber + ", Room Type: " + this.roomType + ", Room Status: " + this.status
				+ ", Room Description: " + this.roomDescription + ", Check In Dates: " + this.checkInDates + ", Check Out Dates: " + this.checkOutDates;
	}
}
