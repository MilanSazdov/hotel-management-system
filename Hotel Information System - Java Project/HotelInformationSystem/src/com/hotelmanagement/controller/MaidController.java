package com.hotelmanagement.controller;

import java.util.ArrayList;

import com.hotelmanagement.model.Maid;
import com.hotelmanagement.model.Gender;
import com.hotelmanagement.model.ProfessionalQualification;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

import com.hotelmanagement.model.Maid;

public class MaidController {
	
	private static MaidController instance;
    private ArrayList<Maid> maidList;

    private MaidController() {
        maidList = new ArrayList<>();
        loadMaidsFromFile();
    }
    
    public static synchronized MaidController getInstance() {
        if (instance == null) {
            instance = new MaidController();
        }
        return instance;
    }
    
    
    public void loadMaidsFromFile() {
        maidList.clear();
        String path = "src/com/hotelmanagement/data/maids.csv";
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Maid maid = new Maid(
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
                maidList.add(maid);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void saveMaidsToFile() {
        String path = "src/com/hotelmanagement/data/maids.csv";
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path))) {
            for (Maid maid : maidList) {
                bw.write(String.format("%s,%s,%s,%s,%s,%s,%s,%d,%f,%s\n",
                    maid.getMaidId(),
                    maid.getName(),
                    maid.getLastName(),
                    maid.getGender(),
                    maid.getBirthDate(),
                    maid.getPhoneNumber(),
                    maid.getUsername(),
                    maid.getPassword(),
                    maid.getWorkingExperience(),
                    maid.getSalary(),
                    maid.getProfessionalQualification()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addMaid(Maid maid) {
        maidList.add(maid);
    }

    public void removeMaid(Maid maid) {
        maidList.remove(maid);
    }

    public void displayAllMaids() {
        for (Maid maid : maidList) {
            System.out.println(maid);
        }
    }

    public ArrayList<Maid> getMaidList() {
        return maidList;
    }
	
	public void setMaidList(ArrayList<Maid> maidList) {
		this.maidList = maidList;
	}
	
	public void modifyMaid(int id, Maid maid) {
		for (Maid m : maidList) {
			if (m.getMaidId() == id) {
				m = maid;
			}
		}
	}
	
	public void searchMaidByName(String name) {
		for (Maid maid : maidList) {
			if (maid.getName().equals(name)) {
				System.out.println(maid);
			}
		}
	}
	
	public void displayMaidById(int id) {
		for (Maid maid : maidList) {
			if (maid.getMaidId() == id) {
				System.out.println(maid);
			}
		}
	}
	
	public void searchMaidByUsername(String email) {
		for (Maid maid : maidList) {
			if (maid.getUsername().equals(email)) {
				System.out.println(maid);
			}
		}
	}
	
	public void searchMaidByPhoneNumber(String phoneNumber) {
		for (Maid maid : maidList) {
			if (maid.getPhoneNumber().equals(phoneNumber)) {
				System.out.println(maid);
			}
		}
	}
	
	public void searchMaidById(int id) {
		for (Maid maid : maidList) {
			if (maid.getMaidId() == id) {
				System.out.println(maid);
			}
		}
	}
	
	public boolean checkUserExists(String username, String password) {
        for (Maid maid : maidList) {
            if (maid.getUsername().equals(username) && maid.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
	
	public ArrayList<Maid> getAllMaids() {
        return new ArrayList<>(maidList); // Return a copy of the list to avoid external modifications
    }
}
