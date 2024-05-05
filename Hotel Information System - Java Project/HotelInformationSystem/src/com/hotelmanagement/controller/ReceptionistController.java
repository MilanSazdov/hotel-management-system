package com.hotelmanagement.controller;

import java.time.LocalDate;
import java.util.ArrayList;

import com.hotelmanagement.model.Receptionist;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomStatus;

public class ReceptionistController {
	
	private static ReceptionistController instance;
    private ArrayList<Receptionist> receptionistList;

    private ReceptionistController() {
        receptionistList = new ArrayList<>();
    }

    public static synchronized ReceptionistController getInstance() {
        if (instance == null) {
            instance = new ReceptionistController();
        }
        return instance;
    }

    public void addReceptionist(Receptionist receptionist) {
        receptionistList.add(receptionist);
    }

    public void removeReceptionist(Receptionist receptionist) {
        receptionistList.remove(receptionist);
    }

    public void displayAllReceptionists() {
        for (Receptionist receptionist : receptionistList) {
            System.out.println(receptionist);
        }
    }

    public ArrayList<Receptionist> getReceptionistList() {
        return receptionistList;
    }
    
    public void displayFreeRoomTypes() {
		System.out.println("Free Room Types:");
		for (Room room : RoomController.getInstance().getRoomList()) {
			if (room.getStatus() == RoomStatus.FREE) {
				System.out.println(room.getRoomType().toString());
			}
		}
	}
    
    public void displayFreeRoomTypesByTimePeriod(LocalDate checkInDate, LocalDate checkOutDate) {
    	System.out.println("Free Room Types:" + checkInDate + " - " + checkOutDate);
		for (Room room : RoomController.getInstance().getRoomList()) {
			if (RoomController.getInstance().isRoomFreeByTimePeriod(room, checkInDate, checkOutDate)) {
				System.out.println(room.getRoomType().toString());
			}
		}
    }
    
    public void modifyReceptionist(int id, Receptionist receptionist) {
    	for (Receptionist r : receptionistList) {
			if (r.getStaffId() == id) {
				r = receptionist;
			}
    	}
    }
    
	public void searchReceptionistByName(String name) {
    	for (Receptionist receptionist : receptionistList) {
    		if (receptionist.getName().equals(name)) {
    			System.out.println(receptionist.toString());
    		}
    	}
	}
	
	public void displayReceptionistById(int id) {
		for (Receptionist receptionist : receptionistList) {
			if (receptionist.getStaffId() == id) {
				System.out.println(receptionist.toString());
			}
		}
	}
	
	public void searchReceptionistByUsername(String email) {
		for (Receptionist receptionist : receptionistList) {
			if (receptionist.getUsername().equals(email)) {
				System.out.println(receptionist.toString());
			}
		}
	}
	
	public void searchReceptionistByPhoneNumber(String phoneNumber) {
		for (Receptionist receptionist : receptionistList) {
			if (receptionist.getPhoneNumber().equals(phoneNumber)) {
				System.out.println(receptionist.toString());
			}
		}
	}
}
