package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.controller.PriceListController;
import com.hotelmanagement.controller.ReceptionistController;

public class Admin extends Staff {
	
	private static int nextId = 0;
	private int adminId;
	
	public Admin(int id, String name, String lastName, Gender gender, LocalDate birthDate, String phoneNumber, String username, String password, int workingExperience, double salary, ProfessionalQualification professionalQualification) {
        super(name, lastName, gender, birthDate, phoneNumber, username, password, workingExperience, salary, professionalQualification);
        this.adminId = id;
	}
	
	public Admin() {
		super();
		this.adminId = nextId++;
	}
	
	// Constructor without ID for manual assignment (if ever needed)
    public Admin(String name, String lastName, Gender gender, LocalDate birthDate, String phoneNumber, String username, String password, int workingExperience, double salary, ProfessionalQualification professionalQualification) {
        super(name, lastName, gender, birthDate, phoneNumber, username, password, workingExperience, salary, professionalQualification);
        this.adminId = nextId++;
    }
	
	
	public int getAdminId() {
		return this.adminId;
	}
	
	public void setAdminId(int id) {
		this.adminId = id;
	}
	
	public void addMaid(Maid maid) {
        MaidController.getInstance().addMaid(maid);
    }
    
    public void removeMaid(Maid maid) {
        MaidController.getInstance().removeMaid(maid);
    }
    
    public void displayMaids() {
        MaidController.getInstance().displayAllMaids();
    }

    public void addReceptionist(Receptionist receptionist) {
        ReceptionistController.getInstance().addReceptionist(receptionist);
    }
    
    public void removeReceptionist(Receptionist receptionist) {
        ReceptionistController.getInstance().removeReceptionist(receptionist);
    }

    public void displayReceptionists() {
        ReceptionistController.getInstance().displayAllReceptionists();
    }
    
	public void addPriceList(PriceList priceList) {
		PriceListController.getInstance().addPriceList(priceList);
	}
	
	public void removePriceList(PriceList priceList) {
		PriceListController.getInstance().removePriceList(priceList);
	}
	
	public void displayAllPriceList() {
		PriceListController.getInstance().displayAllPriceList();
	}
	
	public void displayPriceListById(int id) {
		PriceListController.getInstance().displayPriceListById(id);
	}
	
	
	public void displayAllAdditionalServices() {
		AdditionalServicesController.getInstance().displayAllAdditionalServices();
	}
	
	public void displayAdditionalServiceByName(String name) {
		AdditionalServicesController.getInstance().displayAdditionalServiceByName(name);
	}
	
	
	
	@Override
	public String toString() {
	    return "Admin{" + "id: "+ this.adminId + super.toString() + "}";
	}

}
