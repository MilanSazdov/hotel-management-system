package com.hotelmanagement.controller;

import java.time.LocalDate;
import java.util.ArrayList;

import com.hotelmanagement.model.Receptionist;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomStatus;
import com.hotelmanagement.model.RoomType;
import com.hotelmanagement.model.Receptionist;
import com.hotelmanagement.model.Gender;
import com.hotelmanagement.model.ProfessionalQualification;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;


public class ReceptionistController {
	
	private static ReceptionistController instance;
    private ArrayList<Receptionist> receptionistList;
    
    private ReceptionistController() {
        receptionistList = new ArrayList<>();
        loadReceptionistsFromFile();
    }

    public static synchronized ReceptionistController getInstance() {
        if (instance == null) {
            instance = new ReceptionistController();
        }
        return instance;
    }
    
    public Receptionist getReceptionistByUsername(String username) {
        for (Receptionist receptionist : receptionistList) {
            if (receptionist.getUsername().equals(username)) {
                return receptionist;
            }
        }
        return null; // Returns null if no receptionist with the given username is found
    }
    
	public Receptionist getReceptionistById(int id) {
		for (Receptionist receptionist : receptionistList) {
			if (receptionist.getReceptionistId() == id) {
				return receptionist;
			}
		}
		return null; // Returns null if no receptionist with the given ID is found
	}
	
	public int getNextId() {
		int maxId = 0;
		for (Receptionist receptionist : receptionistList) {
			maxId = Math.max(maxId, receptionist.getReceptionistId());
		}
		return maxId + 1;
	}
    
    public void loadReceptionistsFromFile() {
        receptionistList.clear();
        String path = "src/com/hotelmanagement/data/receptionists.csv";
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Receptionist receptionist = new Receptionist(
                    Integer.parseInt(values[0]), // ID from the file
                    values[1], // Name
                    values[2], // Last Name
                    Gender.valueOf(values[3]), // Gender
                    LocalDate.parse(values[4]), // Birthdate
                    values[5], // Phone Number
                    values[6], // Username
                    values[7], // Password
                    Integer.parseInt(values[8]), // Working Experience
                    Double.parseDouble(values[9]), // Salary
                    ProfessionalQualification.valueOf(values[10]) // Qualification
                );
                receptionistList.add(receptionist);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveReceptionistsToFile() {
        String path = "src/com/hotelmanagement/data/receptionists.csv";
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path))) {
            for (Receptionist receptionist : receptionistList) {
                bw.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%d,%.2f,%s\n",
                    receptionist.getReceptionistId(),  // Integer
                    receptionist.getName(),           // String
                    receptionist.getLastName(),       // String
                    receptionist.getGender(),         // Enum as String
                    receptionist.getBirthDate(),      // LocalDate as String (formatted)
                    receptionist.getPhoneNumber(),    // String
                    receptionist.getUsername(),       // String
                    receptionist.getPassword(),       // String
                    receptionist.getWorkingExperience(), // Integer
                    receptionist.getSalary(),          // Double (formatted to two decimal places)
                    receptionist.getProfessionalQualification() // Enum as String
                ));
            }
        } catch (IOException e) {
            System.err.println("Failed to save receptionists: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public void addReceptionist(Receptionist receptionist) {
        receptionistList.add(receptionist);
        saveReceptionistsToFile();
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
    
    public void displayFreeRoomTypesByTimePeriod(LocalDate checkInDate, LocalDate checkOutDate) {
    	System.out.println("Free Room Types:" + checkInDate + " - " + checkOutDate);
		for (Room room : RoomController.getInstance().getRoomList()) {
			if (RoomController.getInstance().isRoomFreeByTimePeriod(room, checkInDate, checkOutDate)) {
				System.out.println(room.getRoomType().toString());
			}
		}
    }
    
    public void modifyReceptionist(int id, Receptionist receptionist) {
    	for (Receptionist r : receptionistList) {
			if (r.getReceptionistId() == id) {
				r = receptionist;
			}
    	}
    }
    
	public void searchReceptionistByName(String name) {
    	for (Receptionist receptionist : receptionistList) {
    		if (receptionist.getName().equals(name)) {
    			System.out.println(receptionist.toString());
    		}
    	}
	}
	
	public void displayReceptionistById(int id) {
		for (Receptionist receptionist : receptionistList) {
			if (receptionist.getReceptionistId() == id) {
				System.out.println(receptionist.toString());
			}
		}
	}
	
	public void searchReceptionistByUsername(String email) {
		for (Receptionist receptionist : receptionistList) {
			if (receptionist.getUsername().equals(email)) {
				System.out.println(receptionist.toString());
			}
		}
	}
	
	public void searchReceptionistByPhoneNumber(String phoneNumber) {
		for (Receptionist receptionist : receptionistList) {
			if (receptionist.getPhoneNumber().equals(phoneNumber)) {
				System.out.println(receptionist.toString());
			}
		}
	}
	
	public boolean checkUserExists(String username, String password) {
        for (Receptionist receptionist : receptionistList) {
            if (receptionist.getUsername().equals(username) && receptionist.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
	
	public ArrayList<Receptionist> getAllReceptionists() {
        return new ArrayList<>(receptionistList); // Return a copy of the list to avoid external modifications
    }
}
