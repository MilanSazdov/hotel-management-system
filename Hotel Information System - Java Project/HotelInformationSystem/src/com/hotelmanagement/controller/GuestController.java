package com.hotelmanagement.controller;

import java.util.ArrayList;

import com.hotelmanagement.model.Guest;

public class GuestController {
	
	private static GuestController instance;
	private ArrayList<Guest> guestList;
	
	private GuestController() {
		guestList = new ArrayList<Guest>();
	}
	
	public static synchronized GuestController getInstance() {
		if (instance == null) {
			instance = new GuestController();
		}
		return instance;
	}
	
	public void addGuest(Guest guest) {
		guestList.add(guest);
	}
	
	public void removeGuest(Guest guest) {
		guestList.remove(guest);
	}
	
	public void displayAllGuests() {
		for (Guest guest : guestList) {
			System.out.println(guest);
		}
	}
	
	public ArrayList<Guest> getGuestList() {
		return guestList;
	}
	
	public void setGuestList(ArrayList<Guest> guestList) {
		this.guestList = guestList;
	}
	
	
}
