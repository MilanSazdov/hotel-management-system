package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.controller.RoomTypeController;

public class PriceList {

    private LocalDate validFrom;
    private LocalDate validTo;
    static private int nextPriceListId = 1;
    private int priceListId;
    private Map<RoomType, Double> roomTypePrices;
    private Map<AdditionalServices, Double> additionalServicePrices;
    
    
    public PriceList(LocalDate validFrom, LocalDate validTo) {
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.priceListId = nextPriceListId++;
        this.roomTypePrices = new HashMap<>();
        this.additionalServicePrices = new HashMap<>();
    }
    
    public void setPriceForRoomType(RoomType roomType, double price) {
        roomTypePrices.put(roomType, price);
    }

    public double getPriceForRoomType(RoomType roomType) {
        return roomTypePrices.getOrDefault(roomType, 0.0);
    }

    public void setPriceForAdditionalService(AdditionalServices service, double price) {
        additionalServicePrices.put(service, price);
    }

    public double getPriceForAdditionalService(AdditionalServices service) {
        return additionalServicePrices.getOrDefault(service, 0.0);
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
    
    public boolean isValidForDate(LocalDate date) {
        return (date.isEqual(validFrom) || date.isAfter(validFrom)) &&
               (date.isEqual(validTo) || date.isBefore(validTo));
    }

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("PriceList ID: ").append(priceListId)
	      .append("\nValid from: ").append(validFrom)
	      .append("\nValid to: ").append(validTo)
	      .append("\nRoom Types and Prices: ");
	
	    for (Map.Entry<RoomType, Double> entry : roomTypePrices.entrySet()) {
	        sb.append("\n - ").append(entry.getKey().toString()).append(": ").append(entry.getValue());
	    }
	
	    sb.append("\nAdditional Services and Prices: ");
	    for (Map.Entry<AdditionalServices, Double> entry : additionalServicePrices.entrySet()) {
	        sb.append("\n - ").append(entry.getKey().toString()).append(": ").append(entry.getValue());
	    }
	
	    return sb.toString();
	}

}
