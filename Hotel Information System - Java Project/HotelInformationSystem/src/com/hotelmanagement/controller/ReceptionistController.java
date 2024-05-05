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
	
}
