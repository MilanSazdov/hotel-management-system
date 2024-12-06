package com.hotelmanagement.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;

public class ReservationController {
	
	private ArrayList<Reservation> allReservations;
	private static ReservationController instance;
	private static final String FILE_NAME = "src/com/hotelmanagement/data/reservations.csv";

	private ReservationController() {
		allReservations = new ArrayList<>();
		loadReservationsFromFile();
	}

	public static synchronized ReservationController getInstance() {
		if (instance == null) {
			instance = new ReservationController();
		}
		return instance;
	}

	public void addReservation(Reservation reservation) {
		allReservations.add(reservation);
		saveReservationsToFile();
	}

	public void removeReservation(Reservation reservation) {
		allReservations.remove(reservation);
		saveReservationsToFile();
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
		
		saveReservationsToFile();
	}
	
	public void modifyReservationCheckInDate(int id, LocalDate checkInDate) {
		for (Reservation reservation : allReservations) {
			if (reservation.getReservationId() == id) {
				reservation.setCheckInDate(checkInDate);
				System.out.println("Check-in date modified successfully!");
			}
		}
		
		saveReservationsToFile();
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
	    
	    saveReservationsToFile();
	}
	
	public ArrayList<Reservation> getReservationsInDateRange(LocalDate startDate, LocalDate endDate) {
	    return allReservations.stream()
	        .filter(res -> !res.getCheckInDate().isBefore(startDate) && !res.getCheckOutDate().isAfter(endDate))
	        .collect(Collectors.toCollection(ArrayList::new));
	}

	
	private void loadReservationsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                int guestId = Integer.parseInt(parts[1]);
                int roomId = Integer.parseInt(parts[2]);
                int roomTypeId = Integer.parseInt(parts[3]);
                LocalDate checkInDate = LocalDate.parse(parts[4]);
                LocalDate checkOutDate = LocalDate.parse(parts[5]);
                ReservationStatus status = ReservationStatus.valueOf(parts[6]);
                ArrayList<Integer> additionalServiceIds = new ArrayList<>();
                if (!parts[7].isEmpty()) {
                    String[] serviceIds = parts[7].split(";");
                    for (String serviceId : serviceIds) {
                        additionalServiceIds.add(Integer.parseInt(serviceId));
                    }
                }
                double totalCost = Double.parseDouble(parts[8]);
                Reservation reservation = new Reservation(id, guestId, roomId, roomTypeId, checkInDate, checkOutDate, status, additionalServiceIds, totalCost);
                allReservations.add(reservation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	public void saveReservationsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Reservation reservation : allReservations) {
                writer.write(reservation.getReservationId() + "," +
                        reservation.getGuestId() + "," +
                        reservation.getRoomId() + "," +
                        reservation.getRoomTypeId() + "," +
                        reservation.getCheckInDate() + "," +
                        reservation.getCheckOutDate() + "," +
                        reservation.getStatus() + "," +
                        reservation.getAdditionalServiceIds().stream().map(Object::toString).collect(Collectors.joining(";")) + "," +
                        reservation.getTotalCost() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
