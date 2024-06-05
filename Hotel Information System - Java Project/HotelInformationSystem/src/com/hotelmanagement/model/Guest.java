package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Guest extends User {
	
	private String email;
	private String passportNumber;
	private ArrayList<Reservation> reservations;
	private ArrayList<Integer> reservationIds;
	private static int nextGuestId = 1;
	private int guestId;

	public Guest(String name, String lastName, Gender gender, LocalDate birthDate, String phoneNumber,String email, String passportNumber) {
        super(name, lastName, gender, birthDate, phoneNumber, email, passportNumber);
        this.reservations = new ArrayList<>();
        this.reservationIds = new ArrayList<>();
        this.email = email;
        this.passportNumber = passportNumber;
        this.guestId = nextGuestId++;
    }
	
	public Guest(int id, String name, String lastName, Gender gender, LocalDate birthDate, String phoneNumber,String email, String passportNumber, ArrayList<Integer> reservationIds) {
        super(name, lastName, gender, birthDate, phoneNumber, email, passportNumber);
        this.reservationIds = reservationIds;
        this.email = email;
        this.passportNumber = passportNumber;
        this.guestId = id;
    }
	
	public Guest() {
		super();
		this.reservations = new ArrayList<>();
		this.reservationIds = new ArrayList<>();
		this.guestId = nextGuestId++;
	}
	
	public ArrayList<Integer> getReservationIds() {
        return reservationIds;
    }

    public void setReservationIds(ArrayList<Integer> reservationIds) {
        this.reservationIds = reservationIds;
    }
	
	public int getGuestId() {
		return this.guestId;
	}
	
	public void setGuestId(int id) {
		this.guestId = id;
	}
	
	public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassportNumber() {
        return this.passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public ArrayList<Reservation> getReservations() {
        return this.reservations;
    }
    
    public void requestReservation(Reservation reservation) {
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }
        this.reservations.add(reservation);
        System.out.println("Reservation added: " + reservation);
    }
    
    public void removeReservation(Reservation reservation) {
        if (reservations != null) {
            reservations.remove(reservation);
            System.out.println("Reservation " + reservation.getReservationId() + " has been cancelled and removed.");
        }
    }


    

	@Override
	public String toString() {
	    return "Guest{" +
	           "guestId=" + guestId +
	           ", name='" + getName() + '\'' +
	           ", lastName='" + getLastName() + '\'' +
	           ", gender=" + getGender() +
	           ", birthDate=" + getBirthDate() +
	           ", phoneNumber='" + getPhoneNumber() + '\'' +
	           ", username='" + getUsername() + '\'' +
	           ", password='" + getPassword() + '\'' +
	           ", email='" + getEmail() + '\'' +
	           ", passportNumber='" + getPassportNumber() + '\'' +
	           ", reservations=" + reservations +
	           '}';
	}

}
