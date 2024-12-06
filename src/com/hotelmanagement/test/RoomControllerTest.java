package com.hotelmanagement.test;

import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomCategory;
import com.hotelmanagement.model.RoomStatus;
import com.hotelmanagement.model.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RoomControllerTest {

    private RoomController controller;
    private RoomTypeController roomTypeController;

    @BeforeEach
    public void setUp() {
        controller = RoomController.getInstance();
        roomTypeController = RoomTypeController.getInstance();
        controller.getRoomList().clear(); // Clear any existing data for a clean slate
        roomTypeController.getRoomTypeList().clear(); // Clear any existing room types
    }

    @Test
    public void testAddRoom() {
        RoomType roomType = new RoomType(1, 2, RoomCategory.DOUBLE_SINGLE_BED);
        roomTypeController.addRoomType(roomType); // Ensure the room type is added to the controller
        Room room = new Room(1, 101, roomType, RoomStatus.FREE, "Room with two single beds", new ArrayList<>(), new ArrayList<>());
        controller.addRoom(room);

        assertEquals(1, controller.getRoomList().size());
        assertEquals(room, controller.findRoomById(1));
    }

    @Test
    public void testRemoveRoom() {
        RoomType roomType = new RoomType(1, 2, RoomCategory.DOUBLE_SINGLE_BED);
        roomTypeController.addRoomType(roomType); // Ensure the room type is added to the controller
        Room room = new Room(1, 101, roomType, RoomStatus.FREE, "Room with two single beds", new ArrayList<>(), new ArrayList<>());
        controller.addRoom(room);
        controller.removeRoom(room);

        assertEquals(0, controller.getRoomList().size());
    }

    @Test
    public void testDisplayAllRooms() {
        RoomType roomType1 = new RoomType(1, 2, RoomCategory.DOUBLE_SINGLE_BED);
        RoomType roomType2 = new RoomType(2, 1, RoomCategory.SINGLE);
        roomTypeController.addRoomType(roomType1);
        roomTypeController.addRoomType(roomType2);
        Room room1 = new Room(1, 101, roomType1, RoomStatus.FREE, "Room with two single beds", new ArrayList<>(), new ArrayList<>());
        Room room2 = new Room(2, 102, roomType2, RoomStatus.OCCUPIED, "Room with one bed", new ArrayList<>(), new ArrayList<>());
        controller.addRoom(room1);
        controller.addRoom(room2);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.displayAllRooms();

        String expectedOutput = room1.toString() + "\n" + room2.toString() + "\n";
        String actualOutput = outContent.toString().replace("\r", "");

        assertEquals(expectedOutput, actualOutput);

        System.setOut(System.out);
    }

    @Test
    public void testModifyRoomCategory() {
        RoomType roomType = new RoomType(1, 2, RoomCategory.DOUBLE_SINGLE_BED);
        roomTypeController.addRoomType(roomType); // Ensure the room type is added to the controller
        Room room = new Room(1, 101, roomType, RoomStatus.FREE, "Room with two single beds", new ArrayList<>(), new ArrayList<>());
        controller.addRoom(room);

        controller.modifyRoomCategory(1, RoomCategory.SINGLE);

        assertEquals(RoomCategory.SINGLE, controller.findRoomById(1).getRoomType().getCategory());
    }

    @Test
    public void testModifyRoomType() {
        RoomType roomType1 = new RoomType(1, 2, RoomCategory.DOUBLE_SINGLE_BED);
        RoomType roomType2 = new RoomType(2, 1, RoomCategory.SINGLE);
        roomTypeController.addRoomType(roomType1);
        roomTypeController.addRoomType(roomType2);
        Room room = new Room(1, 101, roomType1, RoomStatus.FREE, "Room with two single beds", new ArrayList<>(), new ArrayList<>());
        controller.addRoom(room);

        controller.modifyRoomType(1, roomType2);

        assertEquals(roomType2, controller.findRoomById(1).getRoomType());
    }

    @Test
    public void testIsRoomFreeByTimePeriod() {
        RoomType roomType = new RoomType(1, 2, RoomCategory.DOUBLE_SINGLE_BED);
        roomTypeController.addRoomType(roomType); // Ensure the room type is added to the controller
        Room room = new Room(1, 101, roomType, RoomStatus.FREE, "Room with two single beds", new ArrayList<>(), new ArrayList<>());
        controller.addRoom(room);

        assertTrue(controller.isRoomFreeByTimePeriod(room, LocalDate.now(), LocalDate.now().plusDays(1)));
    }

    @Test
    public void testDisplayFreeRoomTypesByTimePeriod() {
        RoomType roomType1 = new RoomType(1, 2, RoomCategory.DOUBLE_SINGLE_BED);
        RoomType roomType2 = new RoomType(2, 1, RoomCategory.SINGLE);
        roomTypeController.addRoomType(roomType1);
        roomTypeController.addRoomType(roomType2);
        Room room1 = new Room(1, 101, roomType1, RoomStatus.FREE, "Room with two single beds", new ArrayList<>(), new ArrayList<>());
        Room room2 = new Room(2, 102, roomType2, RoomStatus.OCCUPIED, "Room with one bed", new ArrayList<>(), new ArrayList<>());
        controller.addRoom(room1);
        controller.addRoom(room2);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.displayFreeRoomTypesByTimePeriod(LocalDate.of(2024, 7, 2), LocalDate.of(2024, 7, 3));

        String expectedOutput = "Free Room Types from 2024-07-02 to 2024-07-03:\n" +
                                "Room Type: DOUBLE_SINGLE_BED, Number of Beds: 2\n" +
                                "Room Type: SINGLE, Number of Beds: 1\n";
        String actualOutput = outContent.toString().replace("\r", "");

        // Diagnostic prints
        System.err.println("Expected Output:");
        System.err.println(expectedOutput);
        System.err.println("Actual Output:");
        System.err.println(actualOutput);

        assertEquals(expectedOutput, actualOutput);

        System.setOut(System.out);
    }


    @Test
    public void testFindRoomById() {
        RoomType roomType = new RoomType(1, 2, RoomCategory.DOUBLE_SINGLE_BED);
        roomTypeController.addRoomType(roomType); // Ensure the room type is added to the controller
        Room room = new Room(1, 101, roomType, RoomStatus.FREE, "Room with two single beds", new ArrayList<>(), new ArrayList<>());
        controller.addRoom(room);

        Room foundRoom = controller.findRoomById(1);
        assertNotNull(foundRoom);
        assertEquals(room, foundRoom);
    }

    @Test
    public void testGetFreeRoomsByTypeAndPeriod() {
        RoomType roomType1 = new RoomType(1, 2, RoomCategory.DOUBLE_SINGLE_BED);
        RoomType roomType2 = new RoomType(2, 1, RoomCategory.SINGLE);
        roomTypeController.addRoomType(roomType1);
        roomTypeController.addRoomType(roomType2);
        Room room1 = new Room(1, 101, roomType1, RoomStatus.FREE, "Room with two single beds", new ArrayList<>(), new ArrayList<>());
        Room room2 = new Room(2, 102, roomType2, RoomStatus.OCCUPIED, "Room with one bed", new ArrayList<>(), new ArrayList<>());
        controller.addRoom(room1);
        controller.addRoom(room2);

        ArrayList<Room> freeRooms = controller.getFreeRoomsByTypeAndPeriod(roomType1, LocalDate.now(), LocalDate.now().plusDays(1));
        assertEquals(1, freeRooms.size());
        assertEquals(room1, freeRooms.get(0));
    }

    @Test
    public void testUpdateRoomStatus() {
        RoomType roomType = new RoomType(1, 2, RoomCategory.DOUBLE_SINGLE_BED);
        roomTypeController.addRoomType(roomType); // Ensure the room type is added to the controller
        Room room = new Room(1, 101, roomType, RoomStatus.FREE, "Room with two single beds", new ArrayList<>(), new ArrayList<>());
        controller.addRoom(room);

        controller.updateRoomStatus(1, RoomStatus.OCCUPIED);

        assertEquals(RoomStatus.OCCUPIED, controller.findRoomById(1).getStatus());
    }
}
