package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Guest extends User {
	
	private String email;
	private String passportNumber;
	private List<Reservation> reservations;
	private static int nextGuestId = 1;
	private int guestId;

	public Guest(String name, String lastName, Gender gender, LocalDate birthDate, String phoneNumber,String email, String passportNumber) {
        super(name, lastName, gender, birthDate, phoneNumber, email, passportNumber);
        this.reservations = new ArrayList<>();
        this.email = email;
        this.passportNumber = passportNumber;
        this.guestId = nextGuestId++;
    }
	
	public int getGuestId() {
		return this.guestId;
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

    public List<Reservation> getReservations() {
        return this.reservations;
    }
    /*
	public void requestReservation(RoomType roomType, Date checkIn, Date checkOut) {
		Reservation newReservation = new Reservation();
		reservations.add(newReservation);
	}*/
	
	// Gost može da napravi zahtev za rezervaciju određenog tipa sobe za datume koje sam
	// odabere.
    
    @Override
    public String toString() {
        return "Guest{" +
               "name='" + getName() + '\'' +
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
