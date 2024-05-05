package com.hotelmanagement.controller;

import java.util.ArrayList;

import com.hotelmanagement.model.AdditionalServices;

public class AdditionalServicesController {
	
	private static AdditionalServicesController instance;
	private ArrayList<AdditionalServices> additionalServicesList;

	private AdditionalServicesController() {
		additionalServicesList = new ArrayList<>();
	}

	public static synchronized AdditionalServicesController getInstance() {
		if (instance == null) {
			instance = new AdditionalServicesController();
		}
		return instance;
	}

	public void addAdditionalService(AdditionalServices additionalService) {
		additionalServicesList.add(additionalService);
	}

	public void removeAdditionalService(AdditionalServices additionalService) {
		additionalServicesList.remove(additionalService);
	}

	public void displayAllAdditionalServices() {
		for (AdditionalServices additionalService : additionalServicesList) {
			System.out.println(additionalService.toString());
		}
	}
	
	public void displayAdditionalServiceByName(String name) {
		for (AdditionalServices additionalService : additionalServicesList) {
			if (additionalService.getServiceName().equals(name)) {
				System.out.println(additionalService.toString());
			}
		}
	}
	
	public void modifyAdditionalSevicePrice(int id, double price) {
		for (AdditionalServices additionalService : additionalServicesList) {
			if (additionalService.getServiceId() == id) {
				additionalService.setPrice(price);
				System.out.println("Price modified successfully!");
			}
		}
	}

	public ArrayList<AdditionalServices> getAdditionalServicesList() {
		return additionalServicesList;
	}
	
	public void setAdditionalServicesList(ArrayList<AdditionalServices> additionalServicesList) {
		this.additionalServicesList = additionalServicesList;
	}
	
	public void searchAdditionalServiceByName(String name) {
		for (AdditionalServices additionalService : additionalServicesList) {
			if (additionalService.getServiceName().equals(name)) {
				System.out.println(additionalService.toString());
			}
		}
	}
	
	public void searchAdditionalServiceByPrice(double price) {
		for (AdditionalServices additionalService : additionalServicesList) {
			if (additionalService.getPrice() == price) {
				System.out.println(additionalService.toString());
			}
		}
	}
	
	public void searchAdditionalServiceById(int id) {
		for (AdditionalServices additionalService : additionalServicesList) {
            if (additionalService.getServiceId() == id) {
                System.out.println(additionalService.toString());
            }
        }
    }
}
