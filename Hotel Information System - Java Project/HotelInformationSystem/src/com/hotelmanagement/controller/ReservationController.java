package com.hotelmanagement.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.hotelmanagement.model.Guest;
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
	
	public ArrayList<Reservation> getReservationsByGuestId(int guestId) {
	    ArrayList<Reservation> guestReservations = new ArrayList<>();
	    for (Reservation res : allReservations) {
	        if (res.getGuestId() == guestId) {
	            guestReservations.add(res);
	        }
	    }
	    return guestReservations;
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
	
	public Reservation findReservationById(int reservationId) {
	    for (Reservation res : allReservations) {
	        if (res.getReservationId() == reservationId) {
	            return res;
	        }
	    }
	    return null;
	}


	public boolean cancelReservation(int reservationId) {
	    Reservation reservation = findReservationById(reservationId);
	    if (reservation != null && reservation.getStatus().equals(ReservationStatus.WAITING)) {
	        reservation.setStatus(ReservationStatus.CANCELLED);
	        return true; // Successfully updated the status to REJECTED
	    }
	    return false; // Not cancelled due to invalid status or reservation not found
	}


	public ArrayList<Reservation> getReservationsByStatus(ReservationStatus status) {
	    ArrayList<Reservation> filteredReservations = new ArrayList<>();
	    for (Reservation reservation : allReservations) {
	        if (reservation.getStatus() == status) {
	            filteredReservations.add(reservation);
	        }
	    }
	    return filteredReservations;
	}



	public ArrayList<Reservation> getReservationsByGuestAndStatus(int guestId, ReservationStatus status) {
	    ArrayList<Reservation> guestReservations = new ArrayList<>();
	    for (Reservation reservation : allReservations) {
	        if (reservation.getGuestId() == guestId && reservation.getStatus() == status) {
	            guestReservations.add(reservation);
	        }
	    }
	    return guestReservations;
	}

	public void updateReservation(Reservation reservation) {
	    for (int i = 0; i < allReservations.size(); i++) {
	        if (allReservations.get(i).getReservationId() == reservation.getReservationId()) {
	            allReservations.set(i, reservation);
	            System.out.println("Reservation updated in system.");
	            reservation.calculateTotalCost();
	            break;
	        }
	    }
	}
	
	public ArrayList<Reservation> getReservationsInDateRange(LocalDate startDate, LocalDate endDate) {
	    return allReservations.stream()
	        .filter(res -> !res.getCheckInDate().isBefore(startDate) && !res.getCheckOutDate().isAfter(endDate))
	        .collect(Collectors.toCollection(ArrayList::new));
	}



}
