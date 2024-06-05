package com.hotelmanagement.controller;

import java.util.ArrayList;

import com.hotelmanagement.model.Admin;
import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.Maid;
import com.hotelmanagement.model.Receptionist;
import com.hotelmanagement.model.User;

public class DataController {
	private static DataController instance = null;
	private int currentUserId;

	private DataController() {
	}

	public static DataController getInstance() {
		if (instance == null) {
			instance = new DataController();
		}
		return instance;
	}
	
	public void setCurrentUserId(int userId) {
		this.currentUserId = userId;
	}
	
	public int getCurrentUserId() {
		return this.currentUserId;
	}

	public void loadData() {
		AdminController.getInstance().loadAdminsFromFile();
        MaidController.getInstance().loadMaidsFromFile();
        ReceptionistController.getInstance().loadReceptionistsFromFile();
        GuestController.getInstance().loadGuestsFromFile();
        RoomTypeController.getInstance().loadRoomTypesFromFile();
        RoomController.getInstance().loadRoomsFromFile();
        AdditionalServicesController.getInstance().loadAdditionalServicesFromFile();
        PriceListController.getInstance().loadPriceListsFromFile();
	}
	
	public void printPriceLists() {
		PriceListController.getInstance().displayAllPriceList();
	}
	
	// Method to print all users, does not take any parameters
	public void printAllUsers() {
	    printUsers(AdminController.getInstance().getAdminList(), "Admins");
	    printUsers(MaidController.getInstance().getAllMaids(), "Maids");
	    printUsers(ReceptionistController.getInstance().getReceptionistList(), "Receptionists");
	    printUsers(GuestController.getInstance().getGuestList(), "Guests");
	}

	// Helper method to print user details
	private void printUsers(ArrayList<?> users, String userType) {
	    System.out.println(userType + ":");
	    for (Object user : users) {
	        System.out.println(user);  // Directly use toString of each object
	    }
	}
	
	// Get all users from each type
    public ArrayList<Admin> getAllAdmins() {
        return AdminController.getInstance().getAllAdmins();
    }

    public ArrayList<Maid> getAllMaids() {
        return MaidController.getInstance().getAllMaids();
    }

    public ArrayList<Receptionist> getAllReceptionists() {
        return ReceptionistController.getInstance().getAllReceptionists();
    }

    public ArrayList<Guest> getAllGuests() {
        return GuestController.getInstance().getAllGuests();
    }
	
	
	public void loadUser(String username, String password) {
		// Check if the user exists in the system with that username and password
	    AdminController adminController = AdminController.getInstance();
	    if (adminController.checkUserExists(username, password)) {
	        System.out.println("User exists in Admins");
	        return;
	    }

	    MaidController maidController = MaidController.getInstance();
	    if (maidController.checkUserExists(username, password)) {
	        System.out.println("User exists in Maids");
	        return;
	    }

	    ReceptionistController receptionistController = ReceptionistController.getInstance();
	    if (receptionistController.checkUserExists(username, password)) {
	        System.out.println("User exists in Receptionists");
	        return;
	    }

	    GuestController guestController = GuestController.getInstance();
	    if (guestController.checkUserExists(username, password)) {
	        System.out.println("User exists in Guests");
	        return;
	    }

	    System.out.println("User does not exist");
	}
	
	public String authenticateUser(String username, String password) {
        if (AdminController.getInstance().checkUserExists(username, password)) {
        	this.currentUserId = AdminController.getInstance().getAdminByUsername(username).getAdminId();
            return "Admin";
        }
        if (MaidController.getInstance().checkUserExists(username, password)) {
        	this.currentUserId = MaidController.getInstance().getMaidByUsername(username).getMaidId();
            return "Maid";
        }
        if (ReceptionistController.getInstance().checkUserExists(username, password)) {
        	this.currentUserId = ReceptionistController.getInstance().getReceptionistByUsername(username).getReceptionistId();
            return "Receptionist";
        }
        if (GuestController.getInstance().checkUserExists(username, password)) {
        	this.currentUserId = GuestController.getInstance().getGuestByEmail(username).getGuestId();
            return "Guest";
        }
        this.currentUserId = -1;
        return null;  // User does not exist
    }
	
	public void printAllRoomTypes() {
        RoomTypeController roomTypeController = RoomTypeController.getInstance();
        roomTypeController.displayAllRoomTypes();
    }
	
	public void printAllRooms() {
		RoomController roomController = RoomController.getInstance();
		roomController.displayAllRooms();
	}
	
	public void printAllAdditionalServices() {
		AdditionalServicesController additionalServicesController = AdditionalServicesController.getInstance();
		additionalServicesController.displayAllAdditionalServices();
	}
	
	
}
