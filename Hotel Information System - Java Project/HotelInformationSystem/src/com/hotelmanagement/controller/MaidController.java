package com.hotelmanagement.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.hotelmanagement.model.Maid;
import com.hotelmanagement.model.Gender;
import com.hotelmanagement.model.ProfessionalQualification;
import com.hotelmanagement.model.Receptionist;

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
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 11) {
                    System.out.println("Skipping incomplete record: " + Arrays.toString(values));
                    continue;
                }
                int maidId = Integer.parseInt(values[0]);
                String name = values[1];
                String lastName = values[2];
                Gender gender = Gender.valueOf(values[3]);
                LocalDate birthDate = LocalDate.parse(values[4]);
                String phoneNumber = values[5];
                String username = values[6];
                String password = values[7];
                int workingExperience = Integer.parseInt(values[8]);
                double salary = Double.parseDouble(values[9]);
                ProfessionalQualification qualification = ProfessionalQualification.valueOf(values[10]);

                ArrayList<Integer> roomsId = new ArrayList<>();
                if (values.length > 11 && !values[11].isEmpty()) {
                    String[] roomIds = values[11].split(";");
                    for (String id : roomIds) {
                        roomsId.add(Integer.parseInt(id.trim()));
                    }
                }

                Maid maid = new Maid(maidId, name, lastName, gender, birthDate, phoneNumber, username, password, workingExperience, salary, qualification, roomsId);
                maidList.add(maid);
            }
            System.out.println("Maids loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number from file: " + e.getMessage());
        }
    }

    private ArrayList<Integer> parseRoomIds(String data) {
        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(data.split(";"))
                     .map(Integer::parseInt)
                     .collect(Collectors.toCollection(ArrayList::new));
    }

    public void saveMaidsToFile() {
        String path = "src/com/hotelmanagement/data/maids.csv";
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path))) {
            for (Maid maid : maidList) {
                // Convert room IDs list to semicolon-separated string
                String roomsId = maid.getRoomsId().isEmpty() ? "" : maid.getRoomsId().stream()
                                     .map(Object::toString)
                                     .collect(Collectors.joining(";"));

                // Write formatted string to file
                bw.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%d,%.2f,%s,%s\n",
                                       maid.getMaidId(),
                                       maid.getName(),
                                       maid.getLastName(),
                                       maid.getGender().toString(),
                                       maid.getBirthDate().toString(),
                                       maid.getPhoneNumber(),
                                       maid.getUsername(),
                                       maid.getPassword(),
                                       maid.getWorkingExperience(),
                                       maid.getSalary(),
                                       maid.getProfessionalQualification().toString(),
                                       roomsId));
            }
            bw.flush();  // Ensure all data is written to the file
            System.out.println("Maids saved successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    
    public Maid getMaidByUsername(String username) {
        for (Maid maid : maidList) {
            if (maid.getUsername().equals(username)) {
                return maid;
            }
        }
        return null; // Returns null if no maid with the given username is found
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
	
	public Maid getMaidById(int id) {
		for (Maid maid : maidList) {
			if (maid.getMaidId() == id) {
				return maid;
			}
		}
		return null;
	}
	
	public void updateMaid(Maid maid) {
        for (int i = 0; i < maidList.size(); i++) {
            if (maidList.get(i).getMaidId() == maid.getMaidId()) {
                maidList.set(i, maid);
                saveMaidsToFile();
                break;
            }
        }
    }
	
	public Maid getMaidByRoomId(int roomId) {
		for (Maid maid : maidList) {
			if (maid.getRoomsId().contains(roomId)) {
				return maid;
			}
		}
		return null;
	}
	
	public int getNextId() {
		int maxId = 0;
		for (Maid maid : maidList) {
			maxId = Math.max(maxId, maid.getMaidId());
		}
		return maxId + 1;
	}
}
