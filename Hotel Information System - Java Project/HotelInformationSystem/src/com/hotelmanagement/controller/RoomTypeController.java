package com.hotelmanagement.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.hotelmanagement.model.RoomCategory;
import com.hotelmanagement.model.RoomType;

public class RoomTypeController {
	
	private static RoomTypeController instance;
	private ArrayList<RoomType> roomTypeList;
	
	private RoomTypeController() {
		roomTypeList = new ArrayList<>();
	}
	
	public static synchronized RoomTypeController getInstance() {
		if (instance == null) {
			instance = new RoomTypeController();
		}
		return instance;
	}
	
	public void addRoomType(RoomType roomType) {
		roomTypeList.add(roomType);
	}
	
	public void removeRoomType(RoomType roomType) {
		roomTypeList.remove(roomType);
	}
	
	public void displayAllRoomTypes() {
		for (RoomType roomType : roomTypeList) {
			System.out.println(roomType.toString());
		}
	}
	
	public void modifyRoomTypePrice(int id, double price) {
		for (RoomType roomType : roomTypeList) {
			if (roomType.getRoomTypeId() == id) {
				roomType.setPrice(price);
				System.out.println("Price modified successfully!");
			}
		}
	}
	
	public void modifyRoomTypeCategory(int id, RoomCategory category) {
		for (RoomType roomType : roomTypeList) {
			if (roomType.getRoomTypeId() == id) {
				roomType.setCategory(category);
				System.out.println("Category modified successfully!");
			}
		}
	}
	
	public ArrayList<RoomType> getRoomTypeList() {
		return roomTypeList;
	}
	
	public void setRoomTypeList(ArrayList<RoomType> roomTypeList) {
		this.roomTypeList = roomTypeList;
	}
	
	public RoomType searchRoomTypeById(int id) {
		for (RoomType roomType : roomTypeList) {
			if (roomType.getRoomTypeId() == id) {
				return roomType;
			}
		}
		return null;
	}
	
	public void searchRoomTypeByCategory(RoomCategory category) {
		for (RoomType roomType : roomTypeList) {
			if (roomType.getCategory().equals(category)) {
				System.out.println(roomType.toString());
			}
		}
	}
	
	public void searchRoomTypeByPrice(double price) {
		for (RoomType roomType : roomTypeList) {
			if (roomType.getPrice() == price) {
				System.out.println(roomType.toString());
			}
		}
	}
	
	public void loadRoomTypesFromFile() {
        String path = "src/com/hotelmanagement/data/roomtypes.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int roomTypeId = Integer.parseInt(values[0]);
                int numberOfBeds = Integer.parseInt(values[1]);
                RoomCategory category = RoomCategory.valueOf(values[2]);
                double price = Double.parseDouble(values[3]);

                RoomType roomType = new RoomType(roomTypeId, numberOfBeds, category, price);
                roomTypeList.add(roomType);
            }
            System.out.println("Room types loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number from file: " + e.getMessage());
        }
    }

	
	public void saveRoomTypesToFile() {
        String path = "src/com/hotelmanagement/data/roomtypes.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (RoomType roomType : roomTypeList) {
                bw.write(roomType.toCSVString());
                bw.newLine();
            }
            System.out.println("Room types saved successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

}
