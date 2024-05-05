package com.hotelmanagement.model;

public class AdditionalServices {

	private String serviceName;
	private double price;
	static private int nextServiceId = 1;
	private int serviceId;

	public AdditionalServices(String serviceName, double price) {
		this.serviceName = serviceName;
		this.price = price;
		this.serviceId = nextServiceId++;
	}
	
	public int getServiceId() {
        return this.serviceId;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Service id: " + this.serviceId + " Service Name: " + this.serviceName + ", Price: " + this.price;
	}
}
