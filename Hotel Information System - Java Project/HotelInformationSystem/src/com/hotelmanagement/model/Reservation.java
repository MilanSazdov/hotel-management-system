package com.hotelmanagement.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.hotelmanagement.controller.PriceListController;
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
		calculateTotalCost();
	}
	
	public void applyPriceList(PriceList priceList) {
	    for (AdditionalServices service : this.additionalServices) {
	        Double newPrice = priceList.getPriceForAdditionalService(service);
	        if (newPrice != null) {
	            service.setPrice(newPrice);
	        }
	    }
	}
	
	public void calculateTotalCost() {
	    // Uključivanje dana odlaska u izračunavanje broja dana boravka
	    long totalDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate.plusDays(1));  // Dodajemo 1 dan da uključimo dan odlaska

	    LocalDate tempDate = checkInDate;
	    double totalCost = 0.0;

	    // Računanje troškova za svaki dan boravka
	    while (!tempDate.isAfter(checkOutDate)) {
	        PriceList applicablePriceList = PriceListController.getInstance().getApplicablePriceListForDate(tempDate);
	        if (applicablePriceList != null) {
	            double dailyRoomRate = applicablePriceList.getPriceForRoomType(room.getRoomType());
	            totalCost += dailyRoomRate;  // Dodajemo cenu sobe za svaki dan

	            // Dodavanje cene svake dodatne usluge za svaki dan boravka
	            for (AdditionalServices service : additionalServices) {
	                totalCost += applicablePriceList.getPriceForAdditionalService(service);
	            }
	        }
	        tempDate = tempDate.plusDays(1);  // Prelazimo na sledeći dan
	    }

	    this.totalCost = totalCost;
	}


	
	public double getTotalCost() {
		return this.totalCost;
	}
	
	public void setTotalCost(double totalCost) {
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
