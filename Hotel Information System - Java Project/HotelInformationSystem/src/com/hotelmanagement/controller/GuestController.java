package com.hotelmanagement.controller;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.Gender;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import com.hotelmanagement.model.Guest;

public class GuestController {
	
	private static GuestController instance;
	private ArrayList<Guest> guestList;
	
	private GuestController() {
		guestList = new ArrayList<Guest>();
		loadGuestsFromFile();
	}
	
	public void loadGuestsFromFile() {
	    guestList.clear();
	    String path = "src/com/hotelmanagement/data/guests.csv";
	    try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] values = line.split(",");
	            int id = Integer.parseInt(values[0]); // ID iz fajla

	            ArrayList<Integer> reservationIds = null;
	            // Provera da li postoje rezervacije i da li su formatirane sa vitičastim zagradama
	            if (values.length > 8 && values[8].startsWith("{") && values[8].endsWith("}")) {
	                reservationIds = parseReservationIds(values[8]); // Parsiranje ID-eva rezervacija
	            }

	            Guest guest = new Guest(
	                id,
	                values[1], // Ime
	                values[2], // Prezime
	                Gender.valueOf(values[3]), // Pol
	                LocalDate.parse(values[4]), // Datum rođenja
	                values[5], // Broj telefona
	                values[6], // Email
	                values[7], // Broj pasoša
	                reservationIds // Lista ID-eva rezervacija
	            );
	            guestList.add(guest);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	// Pomoćna metoda za parsiranje ID-eva rezervacija
	private ArrayList<Integer> parseReservationIds(String idsString) {
	    ArrayList<Integer> ids = new ArrayList<>();
	    idsString = idsString.replaceAll("\\{|\\}", ""); // Uklanjanje vitičastih zagrada
	    String[] idStrings = idsString.split(",");
	    for (String idStr : idStrings) {
	        try {
	            ids.add(Integer.parseInt(idStr.trim())); // Dodavanje ID-a u listu
	        } catch (NumberFormatException e) {
	            System.err.println("Skipping invalid reservation ID: " + idStr.trim());
	        }
	    }
	    return ids;
	}




	public void saveGuestsToFile() {
	    String path = "src/com/hotelmanagement/data/guests.csv";
	    try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path))) {
	        for (Guest guest : guestList) {
	            String idsString = formatReservationIds(guest.getReservationIds());
	            bw.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s\n",
	                guest.getGuestId(), // ID
	                guest.getName(),
	                guest.getLastName(),
	                guest.getGender(),
	                guest.getBirthDate(),
	                guest.getPhoneNumber(),
	                guest.getEmail(),
	                guest.getPassportNumber(),
	                idsString // Format the reservation IDs
	            ));
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	// Helper method to format reservation IDs into a string
	private String formatReservationIds(ArrayList<Integer> ids) {
	    return "{" + String.join(", ", ids.stream().map(Object::toString).collect(Collectors.toList())) + "}";
	}

    
	public ArrayList<Guest> getAllGuests() {
		return guestList;
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
	
	public boolean checkUserExists(String username, String password) {
        for (Guest guest : guestList) {
            if (guest.getUsername().equals(username) && guest.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
