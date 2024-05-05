package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;

import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.controller.RoomTypeController;

public class PriceList {

    private LocalDate validFrom;
    private LocalDate validTo;
    static private int nextPriceListId = 1;
    private int priceListId;

    public PriceList(LocalDate validFrom, LocalDate validTo) {
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.priceListId = nextPriceListId++;
    }
    
	public void setPriceForRoomType(int id, double price) {
		RoomTypeController.getInstance().modifyRoomTypePrice(id, price);
	}
	
	public void setPriceForAdditionalService(int id, double price) {
		AdditionalServicesController.getInstance().modifyAdditionalSevicePrice(id, price);
	}
    
	public void addRoomType(RoomType roomType) {
		RoomTypeController.getInstance().addRoomType(roomType);
	}
	
	public void removeRoomType(RoomType roomType) {
		RoomTypeController.getInstance().removeRoomType(roomType);
	}
	
	public void addAdditionalService(AdditionalServices additionalService) {
		AdditionalServicesController.getInstance().addAdditionalService(additionalService);
	}
	
	public void removeAdditionalService(AdditionalServices additionalService) {
		AdditionalServicesController.getInstance().removeAdditionalService(additionalService);
	}
	
	public void displayAllRoomTypes() {
		RoomTypeController.getInstance().displayAllRoomTypes();
	}
	
	public void displayAllAdditionalServices() {
		AdditionalServicesController.getInstance().displayAllAdditionalServices();
	}
	
	public void modifyRoomTypePrice(int id, double price) {
		RoomTypeController.getInstance().modifyRoomTypePrice(id, price);
	}
	
	public void modifyAdditionalSevicePrice(int id, double price) {
		AdditionalServicesController.getInstance().modifyAdditionalSevicePrice(id, price);
	}

	public int getPriceListId() {
		return this.priceListId;
	}
    
    public ArrayList<RoomType> getRoomTypes() {
        return RoomTypeController.getInstance().getRoomTypeList();
    }

    public void setRoomTypes(ArrayList<RoomType> roomTypes) {
		RoomTypeController.getInstance().setRoomTypeList(roomTypes);
    }

    public ArrayList<AdditionalServices> getAdditionalServices() {
        return AdditionalServicesController.getInstance().getAdditionalServicesList();
    }

    public void setAdditionalServices(ArrayList<AdditionalServices> additionalServices) {
    	AdditionalServicesController.getInstance().setAdditionalServicesList(additionalServices);
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }
    
    
    @Override
    public String toString() {
        return "PriceList valid from " + validFrom + " to " + validTo +
               "\nRoom Types: " + RoomTypeController.getInstance().getRoomTypeList().toString() +
               "\nAdditional Services: " + AdditionalServicesController.getInstance().getAdditionalServicesList().toString();
    }
}
