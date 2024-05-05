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
	
	public void searchGuestByName(String name) {
		for (Guest guest : guestList) {
			if (guest.getName().equals(name)) {
				System.out.println(guest);
			}
		}
	}
	
	public void modifyGuest(int id, Guest guest) {
		for (Guest g : guestList) {
			if (g.getGuestId() == id) {
				g = guest;
			}
		}
	}
	
	public void displayGuestById(int id) {
		for (Guest guest : guestList) {
			if (guest.getGuestId() == id) {
				System.out.println(guest);
			}
		}
	}
	
	public void searchGuestByUsername(String email) {
		for (Guest guest : guestList) {
			if (guest.getUsername().equals(email)) {
				System.out.println(guest);
			}
		}
	}
	
	public void searchGuestByPhoneNumber(String number) {
		for (Guest guest : guestList) {
			if (guest.getPhoneNumber().equals(number)) {
				System.out.println(guest);
			}
		}
	}
	
	public void searchGuestById(int id) {
		for (Guest guest : guestList) {
			if (guest.getGuestId() == id) {
				System.out.println(guest);
			}
		}
	}
}
