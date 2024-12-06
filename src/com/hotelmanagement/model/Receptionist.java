package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;

public class Receptionist extends Staff{
	
	private static int nextId = 0;
	int receptionistId;
	
	public Receptionist(int id, String name, String lastName, Gender gender, LocalDate birthDate, String phoneNumber, String username, String password, int workingExperience, double salary, ProfessionalQualification professionalQualification) {
        super(name, lastName, gender, birthDate, phoneNumber, username, password, workingExperience, salary, professionalQualification);
        this.receptionistId = Math.max(id, nextId);
    }
	
	public Receptionist(String name, String lastName, Gender gender, LocalDate birthDate, String phoneNumber, String username, String password, int workingExperience, double salary, ProfessionalQualification professionalQualification) {
        super(name, lastName, gender, birthDate, phoneNumber, username, password, workingExperience, salary, professionalQualification);
        this.receptionistId = nextId++;
	}
	
	public Receptionist() {
		super();
		this.receptionistId = nextId++;
	}
	
	public int getReceptionistId() {
		return this.receptionistId;
	}
	
	public void setReceptionistId(int id) {
		this.receptionistId = id;
	}
	
	public void addGuest(Guest guest) {
		GuestController.getInstance().addGuest(guest);
	}
	
	public void displayAllGuests() {
		GuestController.getInstance().displayAllGuests();
	}
	
	public void displayFreeRoomTypes() {
		System.out.println("Free Room Types:");
		for (Room room : RoomController.getInstance().getRoomList()) {
			if (room.getStatus() == RoomStatus.FREE) {
				System.out.println(room.getRoomType().toString());
			}
		}
	}
	
	public void displayAllGuestReservations(int guestId) {
		for (Reservation reservation : ReservationController.getInstance().getAllReservations()) {
			if (reservation.getGuestId() == guestId) {
				System.out.println(reservation.toString());
			}
		}
	}
	
	public ArrayList<Room> getFreeRoomsByType(RoomType type, LocalDate checkInDate, LocalDate checkOutDate) {
        return RoomController.getInstance().getFreeRoomsByTypeAndPeriod(type, checkInDate, checkOutDate);
    }
	
	public void displayAvailableRoomsForType(RoomType type, LocalDate start, LocalDate end) {
	    ArrayList<Room> availableRooms = getFreeRoomsByType(type, start, end);
	    if (availableRooms.isEmpty()) {
	        System.out.println("No available rooms for the specified type and date range.");
	    } else {
	        System.out.println("Available rooms:");
	        for (Room room : availableRooms) {
	            System.out.println(room);
	        }
	    }
	}
	
	@Override
	public String toString() {
	    return "Receptionist{" + super.toString() + "}";
	}
}
