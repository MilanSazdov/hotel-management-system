package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;

import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.controller.RoomTypeController;

public class Reservation {
	
	private Room room;
	private LocalDate checkInDate;
	private LocalDate checkOutDate;
    private ReservationStatus status;
    private static int nextReservationId = 1;
    private int reservationId;
    private int guestId;
    private ArrayList<AdditionalServices> additionalServices;
    private double totalCost;
    
	public Reservation(Room room, LocalDate checkInDate, LocalDate checkOutDate, ReservationStatus status, int guestId, ArrayList<AdditionalServices> additionalServices) {
		this.room = room;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.status = status;
		this.reservationId = nextReservationId++;
		this.guestId = guestId;
		this.additionalServices = additionalServices;
		calculateTotalCost(room);
	}
	
	public double getTotalCost() {
		return this.totalCost;
	}
	
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}
	
	public void calculateTotalCost(Room room) {
		double totalCost = 0;
		totalCost += room.getRoomType().getPrice();
		if (this.additionalServices == null) {
			this.totalCost = totalCost;
			return;
		}
		for (AdditionalServices additionalService : this.additionalServices) {
			totalCost += additionalService.getPrice();
		}
		this.totalCost = totalCost;
	}
	
	public ArrayList<AdditionalServices> getAdditionalServices() {
		return this.additionalServices;
	}
	
	public void setAdditionalServices(ArrayList<AdditionalServices> additionalServices) {
		this.additionalServices = additionalServices;
	}
	
	public int getGuestId() {
		return this.guestId;
	}
	
	public void setGuestId(int guestId) {
		this.guestId = guestId;
	}
	
	public int getReservationId() {
		return this.reservationId;
	}
	
	public Room getRoom() {
		return this.room;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
	public LocalDate getCheckInDate() {
		return this.checkInDate;
	}
	
	public void setCheckInDate(LocalDate checkInDate) {
		this.checkInDate = checkInDate;
	}
	
	public LocalDate getCheckOutDate() {
		return this.checkOutDate;
	}
	
	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}
	
	public ReservationStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(ReservationStatus status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		if (this.additionalServices == null) {
			return "Reservation Id: " + this.reservationId + ", Room: " + this.room + ", Check In Date: "
					+ this.checkInDate + ", Check Out Date: " + this.checkOutDate + ", Status: " + this.status
					+ ", Guest Id: " + this.guestId + " Total price: " + this.totalCost + "\n";
		}
		return "Reservation Id: " + this.reservationId + ", Room: " + this.room + ", Check In Date: " + this.checkInDate
				+ ", Check Out Date: " + this.checkOutDate + ", Status: " + this.status + ", Guest Id: " + this.guestId + ", Additional Services: " + this.additionalServices.toString() + " Total price: " + this.totalCost + "\n";
	}
	
}
