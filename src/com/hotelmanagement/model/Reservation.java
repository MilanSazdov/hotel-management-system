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
	private int roomId;
	private RoomType roomType;
	private int roomTypeId;
	private LocalDate checkInDate;
	private LocalDate checkOutDate;
    private ReservationStatus status;
    private static int nextReservationId = 1;
    private int reservationId;
    private int guestId;
    private ArrayList<AdditionalServices> additionalServices;
    private ArrayList<Integer> additionalServiceIds;
    private double totalCost;
    
    public Reservation(int id, int guestId, int roomId, int roomTypeId, LocalDate checkInDate, LocalDate checkOutDate, ReservationStatus status, ArrayList<Integer> additionalServiceIds, double totalCost) {
		this.reservationId = id;
		this.guestId = guestId;
		this.roomId = roomId;
		this.roomTypeId = roomTypeId;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.status = status;
		this.additionalServiceIds = additionalServiceIds;
		this.totalCost = totalCost;
    }
    
    public Reservation(int id, int guestId, int roomId, int roomTypeId, LocalDate checkInDate, LocalDate checkOutDate, ReservationStatus status, ArrayList<Integer> additionalServiceIds) {
		this.reservationId = id;
		this.guestId = guestId;
		this.roomId = roomId;
		this.roomTypeId = roomTypeId;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.status = status;
		this.additionalServiceIds = additionalServiceIds;
		calculateTotalCost();
    }
    
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
                double dailyRoomRate = applicablePriceList.getPriceForRoomTypeId(roomTypeId);
                totalCost += dailyRoomRate;

                for (Integer serviceId : additionalServiceIds) {
                    totalCost += applicablePriceList.getPriceForAdditionalServiceId(serviceId);
                }
            }
            tempDate = tempDate.plusDays(1);
        }

        this.totalCost = totalCost;
    }
    
    public double getDailyRoomRate(LocalDate date, int roomTypeId) {
        PriceList applicablePriceList = PriceListController.getInstance().getApplicablePriceListForDate(date);
        if (applicablePriceList != null) {
            return applicablePriceList.getPriceForRoomTypeId(roomTypeId);
        }
        return 0.0;
    }
    
    
    public double getDailyAdditionalServiceCost(LocalDate date, ArrayList<Integer> additionalServiceIds) {
        PriceList applicablePriceList = PriceListController.getInstance().getApplicablePriceListForDate(date);
        double totalServiceCost = 0.0;
        if (applicablePriceList != null) {
            for (Integer serviceId : additionalServiceIds) {
                totalServiceCost += applicablePriceList.getPriceForAdditionalServiceId(serviceId);
            }
        }
        return totalServiceCost;
    }
    
    public int getRoomId() {
    	return this.roomId;
    }
    
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	
	public int getRoomTypeId() {
		return this.roomTypeId;
	}
	
	public void setRoomTypeId(int roomTypeId) {
		this.roomTypeId = roomTypeId;
	}
	
	public ArrayList<Integer> getAdditionalServiceIds() {
		return this.additionalServiceIds;
	}
	
	public void setAdditionalServiceIds(ArrayList<Integer> additionalServiceIds) {
		this.additionalServiceIds = additionalServiceIds;
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
