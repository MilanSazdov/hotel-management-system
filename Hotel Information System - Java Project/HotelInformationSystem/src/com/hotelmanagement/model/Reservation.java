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
	private RoomType roomType;
	private LocalDate checkInDate;
	private LocalDate checkOutDate;
    private ReservationStatus status;
    private static int nextReservationId = 1;
    private int reservationId;
    private int guestId;
    private ArrayList<AdditionalServices> additionalServices;
    private double totalCost;
    
	public Reservation(Room room, RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate, ReservationStatus status, int guestId, ArrayList<AdditionalServices> additionalServices) {
		this.room = room;
		this.roomType = roomType;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.status = status;
		this.reservationId = nextReservationId++;
		this.guestId = guestId;
		this.additionalServices = additionalServices;
		calculateTotalCost();
	}
    
    public void calculateTotalCost() {
        long totalDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate.plusDays(1));  // Including the checkout day
        LocalDate tempDate = checkInDate;
        double totalCost = 0.0;

        while (!tempDate.isAfter(checkOutDate)) {
            PriceList applicablePriceList = PriceListController.getInstance().getApplicablePriceListForDate(tempDate);
            if (applicablePriceList != null) {
                double dailyRoomRate = applicablePriceList.getPriceForRoomTypeId(roomType.getRoomTypeId());
                totalCost += dailyRoomRate;

                for (AdditionalServices service : additionalServices) {
                    totalCost += applicablePriceList.getPriceForAdditionalServiceId(service.getServiceId());
                }
            }
            tempDate = tempDate.plusDays(1);
        }

        this.totalCost = totalCost;
    }

	public RoomType getRoomType() {
		return this.roomType;
	}
	
	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
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
        String servicesInfo = (additionalServices != null) ? additionalServices.toString() : "No additional services";
        return String.format("Reservation Id: %d, Room: %s, Check In Date: %s, Check Out Date: %s, Status: %s, Guest Id: %d, Additional Services: %s, Total price: %.2f\n",
                             reservationId, room, checkInDate, checkOutDate, status, guestId, servicesInfo, totalCost);
    }
	
}
