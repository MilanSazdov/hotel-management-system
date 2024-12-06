package com.hotelmanagement.controller;

import java.util.ArrayList;
import com.hotelmanagement.model.Admin;
import com.hotelmanagement.model.Gender;
import com.hotelmanagement.model.ProfessionalQualification;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
	
	private ArrayList<Admin> adminList;
	
	private AdminController() {
		adminList = new ArrayList<Admin>();
		loadAdminsFromFile();
	}
	
	private static AdminController instance;
	
	public static synchronized AdminController getInstance() {
		if (instance == null) {
			instance = new AdminController();
		}
		return instance;
	}
	
	public Admin getAdminByUsername(String username) {
	    for (Admin admin : adminList) {
	        if (admin.getUsername().equals(username)) {
	            return admin;
	        }
	    }
	    return null; // Vraća null ako admin sa datim korisničkim imenom nije pronađen
	}

	public Admin getAdminById(int id) {
		for (Admin admin : adminList) {
			if (admin.getAdminId() == id) {
				return admin;
			}
		}
		return null;
	}
	
	public void addAdmin(Admin admin) {
		adminList.add(admin);
    }
	
	public void removeAdmin(Admin admin) {
		adminList.remove(admin);
	}
	
	public void displayAllAdmins() {
		for (Admin admin : adminList) {
			System.out.println(admin.toString());
		}
	}
	
	public void displayAdminById(int id) {
		for (Admin admin : adminList) {
			if (admin.getAdminId() == id) {
				System.out.println(admin.toString());
			}
		}
	}
	
	public void modifyAdmin(int id, Admin updatedAdmin) {
	    for (int i = 0; i < adminList.size(); i++) {
	        if (adminList.get(i).getAdminId() == id) {
	            adminList.set(i, updatedAdmin);
	            saveAdminsToFile();
	            break;
	        }
	    }
	}
	
	public ArrayList<Admin> getAdminList() {
		return this.adminList;
	}
	
	public void setAdminList(ArrayList<Admin> adminList) {
		this.adminList = adminList;
	}
	
	public void searchAdminByName(String name) {
        for (Admin admin : adminList) {
            if (admin.getName().equals(name)) {
                System.out.println(admin.toString());
            }
        }
    }
	
	public void searchAdminByUsername(String email) {
		for (Admin admin : adminList) {
			if (admin.getUsername().equals(email)) {
				System.out.println(admin.toString());
			}
		}
	}
	
	public void searchAdminByPhoneNumber(String number) {
		for (Admin admin : adminList) {
			if (admin.getPhoneNumber().equals(number)) {
				System.out.println(admin.toString());
			}
		}
	}
	
	public void searchAdminById(int id) {
		for (Admin admin : adminList) {
			if (admin.getAdminId() == id) {
				System.out.println(admin.toString());
			}
		}
	}
	
	public void loadAdminsFromFile() {
	    adminList.clear();
	    String path = "src/com/hotelmanagement/data/admins.csv";
	    try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] values = line.split(",");
	            Admin admin = new Admin(
	                Integer.parseInt(values[0]),  // ID from the file
	                values[1],  // Name
	                values[2],  // Last Name
	                Gender.valueOf(values[3]),  // Gender
	                LocalDate.parse(values[4]),  // Birthdate
	                values[5],  // Phone Number
	                values[6],  // Username
	                values[7],  // Password
	                Integer.parseInt(values[8]),  // Working Experience
	                Double.parseDouble(values[9]),  // Salary
	                ProfessionalQualification.valueOf(values[10])  // Qualification
	            );
	            adminList.add(admin);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public void saveAdminsToFile() {
	    String path = "src/com/hotelmanagement/data/admins.csv";
	    try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path))) {
	        for (Admin admin : adminList) {
	            bw.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%d,%f,%s\n",
	                admin.getAdminId(),
	                admin.getName(),
	                admin.getLastName(),
	                admin.getGender(),
	                admin.getBirthDate(),
	                admin.getPhoneNumber(),
	                admin.getUsername(),
	                admin.getPassword(),
	                admin.getWorkingExperience(),
	                admin.getSalary(),
	                admin.getProfessionalQualification()
	            ));
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
    
    public boolean checkUserExists(String username, String password) {
        for (Admin admin : adminList) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Admin> getAllAdmins() {
        return new ArrayList<>(adminList); // Return a copy of the list to avoid external modifications
    }
}
