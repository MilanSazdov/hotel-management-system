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
        loadRoomTypesFromFile();
    }
    
    public static synchronized RoomTypeController getInstance() {
        if (instance == null) {
            instance = new RoomTypeController();
        }
        return instance;
    }
    
    public void modifyRoomTypeCategory(int id, RoomCategory category) {
        for (RoomType roomType : roomTypeList) {
            if (roomType.getRoomTypeId() == id) {
                roomType.setCategory(category);
                System.out.println("Category modified successfully!");
                saveRoomTypesToFile();
                return;  // Exit once the modification is done
            }
        }
        System.out.println("No room type found with ID: " + id);  // If no room type matches the ID
    }
    
    public void addRoomType(RoomType roomType) {
        roomTypeList.add(roomType);
        saveRoomTypesToFile();
    }
    
    public void removeRoomType(RoomType roomType) {
        roomTypeList.remove(roomType);
        saveRoomTypesToFile();
    }
    
    public void displayAllRoomTypes() {
        for (RoomType roomType : roomTypeList) {
            System.out.println(roomType.toString());
        }
    }
    
    public ArrayList<RoomType> getRoomTypeList() {
        return roomTypeList;
    }
    
    public void setRoomTypeList(ArrayList<RoomType> roomTypeList) {
        this.roomTypeList = roomTypeList;
        saveRoomTypesToFile();
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
    
    public void loadRoomTypesFromFile() {
        String path = "src/com/hotelmanagement/data/roomtypes.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int roomTypeId = Integer.parseInt(values[0]);
                int numberOfBeds = Integer.parseInt(values[1]);
                RoomCategory category = RoomCategory.valueOf(values[2]);

                RoomType roomType = new RoomType(roomTypeId, numberOfBeds, category);
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
    
    public void loadRoomTypesFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            roomTypeList.clear(); // Clear the list before loading
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int roomTypeId = Integer.parseInt(values[0]);
                int numberOfBeds = Integer.parseInt(values[1]);
                RoomCategory category = RoomCategory.valueOf(values[2]);

                RoomType roomType = new RoomType(roomTypeId, numberOfBeds, category);
                roomTypeList.add(roomType);
            }
            System.out.println("Room types loaded successfully from " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number from file: " + e.getMessage());
        }
    }

    public void saveRoomTypesToFile(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (RoomType roomType : roomTypeList) {
                bw.write(roomType.toCSVString());
                bw.newLine();
            }
            System.out.println("Room types saved successfully to " + filePath);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
