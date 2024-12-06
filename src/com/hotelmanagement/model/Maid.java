package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Maid extends Staff {
	
	private static int nextId = 0;
	int maidId;
	ArrayList<Integer> roomsId;
	
	public Maid(int id, String name, String lastName, Gender gender, LocalDate birthDate, String phoneNumber, String username, String password, int workingExperience, double salary, ProfessionalQualification professionalQualification, ArrayList<Integer> roomsId) {
        super(name, lastName, gender, birthDate, phoneNumber, username, password, workingExperience, salary, professionalQualification);
        this.maidId = id;
        this.roomsId = new ArrayList<>(roomsId);  // Initialize with a copy of the provided list
        nextId = Math.max(id + 1, nextId);
    }

    public Maid(String name, String lastName, Gender gender, LocalDate birthDate, String phoneNumber, String username, String password, int workingExperience, double salary, ProfessionalQualification professionalQualification) {
        super(name, lastName, gender, birthDate, phoneNumber, username, password, workingExperience, salary, professionalQualification);
        this.maidId = nextId++;
        this.roomsId = new ArrayList<>();  // Initialize an empty list
    }

    public ArrayList<Integer> getRoomsId() {
        return new ArrayList<>(roomsId);  // Return a copy of the list to prevent external modifications
    }
    
	public void addRoomId(int roomId) {
        roomsId.add(roomId);
    }

    public void setRoomsId(ArrayList<Integer> roomsId) {
        this.roomsId = new ArrayList<>(roomsId);  // Assign a copy of the list
    }
	
	public Maid() {
		super();
		this.maidId = nextId++;
	}
	
	public int getMaidId() {
		return this.maidId;
	}
	
	public void setMaidId(int id) {
		this.maidId = id;
	}
	
	
	// Method to return the number of rooms assigned to this maid for cleaning
    public int numberOfCleaningRooms() {
        return roomsId.size();
    }
	 
    
	@Override
	public String toString() {
	    return "Maid{" + super.toString() + ", roomsId=" + roomsId + "}";
	}

}
