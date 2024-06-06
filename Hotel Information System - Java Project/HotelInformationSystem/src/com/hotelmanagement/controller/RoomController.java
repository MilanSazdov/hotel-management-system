package com.hotelmanagement.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomCategory;
import com.hotelmanagement.model.RoomStatus;
import com.hotelmanagement.model.RoomType;

public class RoomController {
	
	private static RoomController instance;
	private ArrayList<Room> roomList;

	private RoomController() {
		roomList = new ArrayList<>();
	}

	public static synchronized RoomController getInstance() {
		if (instance == null) {
			instance = new RoomController();
		}
		return instance;
	}

	public void addRoom(Room room) {
		roomList.add(room);
	}

	public void removeRoom(Room room) {
		roomList.remove(room);
	}

	public void displayAllRooms() {
		for (Room room : roomList) {
			System.out.println(room.toString());
		}
	}

	public ArrayList<Room> getRoomList() {
		return roomList;
	}

	public void modifyRoomCategory(int id, RoomCategory category) {
		for (Room room : roomList) {
			if (room.getRoomId() == id) {
				RoomTypeController.getInstance().modifyRoomTypeCategory(room.getRoomType().getRoomTypeId(), category);
			}
		}
	}
	
	public void modifyRoomType(int id, RoomType roomType) {
		for (Room room : roomList) {
			if (room.getRoomId() == id) {
				room.setRoomType(roomType);
			}
		}
	}
	
	public boolean isRoomFreeByTimePeriod(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
		if (room.getStatus() == RoomStatus.FREE) {
			return true;
		}
        return room.isRoomFreeByTimePeriod(checkInDate, checkOutDate);
    }
	
	public void displayFreeRoomTypesByTimePeriod(LocalDate checkInDate, LocalDate checkOutDate) {
        System.out.println("Free Room Types from " + checkInDate + " to " + checkOutDate + ":");
        for (Room room : roomList) {
            if (isRoomFreeByTimePeriod(room, checkInDate, checkOutDate)) {
                System.out.println(room.getRoomType().toString());
            }
        }
    }
	
	public void displayRoomById(int id) {
		for (Room room : roomList) {
			if (room.getRoomId() == id) {
				System.out.println(room.toString());
			}
		}
	}
	
	public void searchRoomById(int id) {
		for (Room room : roomList) {
			if (room.getRoomId() == id) {
				System.out.println(room.toString());
			}
		}
	}
	
	public ArrayList<Room> getFreeRoomsByTypeAndPeriod(RoomType type, LocalDate checkInDate, LocalDate checkOutDate) {
        ArrayList<Room> freeRooms = new ArrayList<>();
        for (Room room : roomList) {
            if (room.getRoomType().equals(type) && isRoomFreeByTimePeriod(room, checkInDate, checkOutDate)) {
                freeRooms.add(room);
            }
        }
        return freeRooms;
    }
	
	public void loadRoomsFromFile() {
	    String path = "src/com/hotelmanagement/data/rooms.csv";
	    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] values = line.split(",");
	            // Parsing the CSV values according to the new order
	            int roomId = Integer.parseInt(values[0]); // Room ID is now at position 0
	            int roomNumber = Integer.parseInt(values[1]); // Room number is now at position 1
	            int roomTypeId = Integer.parseInt(values[2]); // Room type ID is now at position 2
	            RoomStatus status = RoomStatus.valueOf(values[3]); // Status is now at position 3
	            String roomDescription = values[4]; // Room description is now at position 4
	            ArrayList<LocalDate> checkInDates = parseDates(values.length > 5 ? values[5] : ""); // Check-in dates are now at position 5
	            ArrayList<LocalDate> checkOutDates = parseDates(values.length > 6 ? values[6] : ""); // Check-out dates are now at position 6

	            // Fetch the RoomType object using the Room Type ID
	            RoomType roomType = RoomTypeController.getInstance().searchRoomTypeById(roomTypeId);
	            if (roomType == null) {
	                System.out.println("Room type not found for Room Type ID: " + roomTypeId);
	                continue; // Skip this room if room type is not found
	            }

	            Room room = new Room(roomId, roomNumber, roomType, status, roomDescription, checkInDates, checkOutDates);
	            roomList.add(room);
	        }
	        System.out.println("Rooms loaded successfully.");
	    } catch (IOException e) {
	        System.out.println("Error reading from file: " + e.getMessage());
	    } catch (NumberFormatException e) {
	        System.out.println("Error parsing number from file: " + e.getMessage());
	    }
	}


    // Helper method to parse dates from a comma-separated string
    private ArrayList<LocalDate> parseDates(String dateString) {
        ArrayList<LocalDate> dates = new ArrayList<>();
        if (dateString != null && !dateString.trim().isEmpty()) {
            String[] dateStrings = dateString.split(";");
            for (String dateStr : dateStrings) {
                dates.add(LocalDate.parse(dateStr.trim()));
            }
        }
        return dates;
    }

    // Save rooms to a CSV file
    public void saveRoomsToFile() {
        String path = "src/com/hotelmanagement/data/rooms.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Room room : roomList) {
                bw.write(room.toCSVString());
                bw.newLine();
            }
            System.out.println("Rooms saved successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    
	public Room findRoomById(int id) {
		for (Room room : roomList) {
			if (room.getRoomId() == id) {
				return room;
			}
		}
		return null;
	}
	
	// Method to retrieve all rooms
    public ArrayList<Room> getAllRooms() {
        return new ArrayList<>(roomList); // Return a copy of the rooms list to prevent external modifications
    }

 // Method to update the status of a room
    public void updateRoomStatus(int roomId, RoomStatus newStatus) {
        Room room = findRoomById(roomId);
        if (room != null) {
            room.setStatus(newStatus);
            System.out.println("Room " + roomId + " status updated to " + newStatus);
        } else {
            System.out.println("Room with ID " + roomId + " not found.");
        }
    }
    
    public void updateRoomAttributes(Room updatedRoom) {
        for (Room room : roomList) {
            if (room.getRoomId() == updatedRoom.getRoomId()) {
                room.setStatus(updatedRoom.getStatus());
                break;
            }
        }
    }
}
