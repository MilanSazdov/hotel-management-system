package com.hotelmanagement.model;

public class AdditionalServices {

    private String serviceName;
    static private int nextServiceId = 1;
    private int serviceId;

    public AdditionalServices(String serviceName) {
        this.serviceName = serviceName;
        this.serviceId = nextServiceId++;
    }
    
    public AdditionalServices(int id, String serviceName) {
        this.serviceName = serviceName;
        this.serviceId = id;
    }
    
    public String toCSVString() {
        return serviceId + "," + serviceName;
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

    @Override
    public String toString() {
        return "Service id: " + this.serviceId + ", Service Name: " + this.serviceName;
    }
}
