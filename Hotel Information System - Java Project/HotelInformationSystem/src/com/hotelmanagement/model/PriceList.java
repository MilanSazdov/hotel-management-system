package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.controller.RoomTypeController;

public class PriceList {

    private LocalDate validFrom;
    private LocalDate validTo;
    static private int nextPriceListId = 1;
    private int priceListId;
    private Map<Integer, Double> roomTypePrices;
    private Map<Integer, Double> additionalServicePrices;
    
    // Constructor for PriceList from file
    public PriceList(int id, LocalDate validFrom, LocalDate validTo, Map<Integer, Double> roomTypePrices, Map<Integer, Double> additionalServicePrices) {
        this.priceListId = id;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.roomTypePrices = roomTypePrices;
        this.additionalServicePrices = additionalServicePrices;
        nextPriceListId = Math.max(nextPriceListId, id + 1);
    }
    
    public PriceList(LocalDate validFrom, LocalDate validTo) {
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.priceListId = nextPriceListId++;
        this.roomTypePrices = new HashMap<>();
        this.additionalServicePrices = new HashMap<>();
    }
    
    public void setPriceForRoomTypeId(int roomTypeId, double price) {
        roomTypePrices.put(roomTypeId, price);
    }

    public double getPriceForRoomTypeId(int roomTypeId) {
        return roomTypePrices.getOrDefault(roomTypeId, 0.0);
    }

    public void setPriceForAdditionalServiceId(int serviceId, double price) {
        additionalServicePrices.put(serviceId, price);
    }

    public double getPriceForAdditionalServiceId(int serviceId) {
        return additionalServicePrices.getOrDefault(serviceId, 0.0);
    }
    
    public int getPriceListId() {
        return this.priceListId;
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
    
    public String toCSVString() {
        return String.format("%d,%s,%s,{%s},{%s}",
            priceListId,
            validFrom,
            validTo,
            mapToString(roomTypePrices),
            mapToString(additionalServicePrices));
    }

    private String mapToString(Map<Integer, Double> map) {
        return map.entrySet().stream()
                  .map(entry -> entry.getKey() + ":" + entry.getValue())
                  .collect(Collectors.joining(";"));
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PriceList ID: ").append(priceListId)
          .append("\nValid from: ").append(validFrom)
          .append("\nValid to: ").append(validTo)
          .append("\nRoom Types and Prices: ");
        roomTypePrices.forEach((id, price) -> sb.append("\n - ID: ").append(id).append(", Price: ").append(price));
        sb.append("\nAdditional Services and Prices: ");
        additionalServicePrices.forEach((id, price) -> sb.append("\n - ID: ").append(id).append(", Price: ").append(price));
        return sb.toString();
    }
}
