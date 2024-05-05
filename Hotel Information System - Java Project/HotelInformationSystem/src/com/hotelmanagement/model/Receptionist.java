package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.Date;

import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;

public class Receptionist extends Staff{
	
	public Receptionist(String name, String lastName, Gender gender, LocalDate birthDate, String phoneNumber, String username, String password, int workingExperience, double salary, ProfessionalQualification professionalQualification) {
        super(name, lastName, gender, birthDate, phoneNumber, username, password, workingExperience, salary, professionalQualification);
    }
	
	public Receptionist() {
		super();
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
	
	@Override
	public String toString() {
	    return "Receptionist{" + super.toString() + "}";
	}
}
