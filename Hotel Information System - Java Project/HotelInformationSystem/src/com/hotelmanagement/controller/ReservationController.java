package com.hotelmanagement.controller;

import java.time.LocalDate;
import java.util.ArrayList;

import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;

public class ReservationController {
	
	private ArrayList<Reservation> allReservations;
	private static ReservationController instance;

	private ReservationController() {
		allReservations = new ArrayList<>();
	}

	public static synchronized ReservationController getInstance() {
		if (instance == null) {
			instance = new ReservationController();
		}
		return instance;
	}

	public void addReservation(Reservation reservation) {
		allReservations.add(reservation);
	}

	public void removeReservation(Reservation reservation) {
		allReservations.remove(reservation);
	}

	public void displayAllReservations() {
		for (Reservation reservation : allReservations) {
			System.out.println(reservation.toString());
		}
	}

	public void displayReservationById(int id) {
		for (Reservation reservation : allReservations) {
			if (reservation.getReservationId() == id) {
				System.out.println(reservation.toString());
			}
		}
	}
	
	public void modifyReservationStatus(int id, ReservationStatus status) {
		for (Reservation reservation : allReservations) {
			if (reservation.getReservationId() == id) {
				reservation.setStatus(status);
				System.out.println("Status modified successfully!");
			}
		}
	}
	
	public void modifyReservationCheckInDate(int id, LocalDate checkInDate) {
		for (Reservation reservation : allReservations) {
			if (reservation.getReservationId() == id) {
				reservation.setCheckInDate(checkInDate);
				System.out.println("Check-in date modified successfully!");
			}
		}
	}
	
	public void modifyReservationCheckOutDate(int id, LocalDate checkOutDate) {
		for (Reservation reservation : allReservations) {
			if (reservation.getReservationId() == id) {
				reservation.setCheckOutDate(checkOutDate);
				System.out.println("Check-out date modified successfully!");
			}
		}
	}
	
	

	public ArrayList<Reservation> getAllReservations() {
		return allReservations;
	}
	
	public void searchReservationById(int id) {
		for (Reservation reservation : allReservations) {
			if (reservation.getReservationId() == id) {
				System.out.println(reservation.toString());
			}
		}
	}
}
