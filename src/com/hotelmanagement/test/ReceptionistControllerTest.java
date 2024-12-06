package com.hotelmanagement.test;

import com.hotelmanagement.controller.ReceptionistController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.model.Gender;
import com.hotelmanagement.model.ProfessionalQualification;
import com.hotelmanagement.model.Receptionist;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomStatus;
import com.hotelmanagement.model.RoomType;
import com.hotelmanagement.model.RoomCategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ReceptionistControllerTest {

    private ReceptionistController controller;

    @BeforeEach
    public void setUp() {
        // Inicijalizacija instance kontrolera
        controller = ReceptionistController.getInstance();

        // Dodavanje test podataka
        controller.getReceptionistList().clear(); // Očisti prethodne podatke

        Receptionist receptionist = new Receptionist(1, "Test", "User", Gender.FEMALE, LocalDate.of(1990, 1, 1), "1234567890", "testuser", "password", 5, 2000.0, ProfessionalQualification.HIGH_SCHOOL);
        controller.addReceptionist(receptionist);

        // Dodavanje test soba
        RoomController.getInstance().getRoomList().clear();
        RoomType roomType1 = new RoomType(1, 2, RoomCategory.DOUBLE_SINGLE_BED);
        RoomType roomType2 = new RoomType(2, 1, RoomCategory.SINGLE);
        Room room1 = new Room(1, 101, roomType1, RoomStatus.FREE, "Room with two single beds", new ArrayList<>(), new ArrayList<>());
        Room room2 = new Room(2, 102, roomType2, RoomStatus.OCCUPIED, "Room with one bed", new ArrayList<>(), new ArrayList<>());
        RoomController.getInstance().addRoom(room1);
        RoomController.getInstance().addRoom(room2);
    }

    @Test
    public void testAddReceptionist() {
        // Test dodavanja recepcionara
        Receptionist receptionist = new Receptionist(controller.getNextId(), "Anna", "Smith", Gender.FEMALE, LocalDate.of(1985, 1, 1), "1234567890", "anna", "password", 5, 2000.0, ProfessionalQualification.HIGH_SCHOOL);
        controller.addReceptionist(receptionist);

        assertEquals(2, controller.getReceptionistList().size());
        assertEquals(receptionist, controller.getReceptionistById(receptionist.getReceptionistId()));
    }

    @Test
    public void testRemoveReceptionist() {
        // Test uklanjanja recepcionara
        Receptionist receptionist = new Receptionist(controller.getNextId(), "Jane", "Doe", Gender.FEMALE, LocalDate.of(1990, 2, 2), "0987654321", "jane", "password123", 3, 1800.0, ProfessionalQualification.COLLEGE);
        controller.addReceptionist(receptionist);
        controller.removeReceptionist(receptionist);

        assertEquals(1, controller.getReceptionistList().size()); // Očekuje se 1 jer je jedan test korisnik dodan u setUp
    }

    @Test
    public void testGetReceptionistByUsername() {
        // Test preuzimanja recepcionara po korisničkom imenu
        Receptionist receptionist = new Receptionist(controller.getNextId(), "Alice", "Johnson", Gender.FEMALE, LocalDate.of(1980, 3, 3), "1122334455", "alice", "password321", 10, 2500.0, ProfessionalQualification.BASIC_SCHOOL);
        controller.addReceptionist(receptionist);

        Receptionist foundReceptionist = controller.getReceptionistByUsername("alice");
        assertNotNull(foundReceptionist);
        assertEquals(receptionist, foundReceptionist);
    }

    @Test
    public void testCheckUserExists() {
        // Test provere postojanja korisnika
        Receptionist receptionist = new Receptionist(controller.getNextId(), "Eve", "White", Gender.FEMALE, LocalDate.of(1975, 4, 4), "2233445566", "eve", "mypassword", 8, 2200.0, ProfessionalQualification.HIGH_SCHOOL);
        controller.addReceptionist(receptionist);

        boolean userExists = controller.checkUserExists("eve", "mypassword");
        assertTrue(userExists);

        boolean userDoesNotExist = controller.checkUserExists("eve", "wrongpassword");
        assertFalse(userDoesNotExist);
    }

    @Test
    public void testGetReceptionistById() {
        // Test preuzimanja recepcionara po ID-u
        Receptionist receptionist = new Receptionist(controller.getNextId(), "Linda", "Brown", Gender.FEMALE, LocalDate.of(1988, 5, 5), "3344556677", "linda", "securepass", 6, 2100.0, ProfessionalQualification.COLLEGE);
        controller.addReceptionist(receptionist);

        Receptionist foundReceptionist = controller.getReceptionistById(receptionist.getReceptionistId());
        assertNotNull(foundReceptionist);
        assertEquals(receptionist, foundReceptionist);
    }

    @Test
    public void testGetNextId() {
        // Test preuzimanja sledećeg ID-a
        int currentMaxId = controller.getReceptionistList().stream()
                                 .mapToInt(Receptionist::getReceptionistId)
                                 .max()
                                 .orElse(0);
        int nextId = controller.getNextId();
        assertEquals(currentMaxId + 1, nextId);
    }

    @Test
    public void testLoadReceptionistsFromFile() {
        // Test učitavanja recepcionara iz CSV fajla
        controller.loadReceptionistsFromFile();
        assertFalse(controller.getReceptionistList().isEmpty());
    }

    @Test
    public void testSaveReceptionistsToFile() {
        // Test čuvanja recepcionara u CSV fajl
        Receptionist receptionist = new Receptionist(controller.getNextId(), "Monica", "Geller", Gender.FEMALE, LocalDate.of(1970, 7, 7), "5566778899", "monica", "monicapass", 12, 2400.0, ProfessionalQualification.COLLEGE);
        controller.addReceptionist(receptionist);

        controller.saveReceptionistsToFile();
        // Provera sadržaja fajla može biti dodata ovde
    }

    @Test
    public void testUpdateReceptionist() {
        // Test ažuriranja recepcionara
        Receptionist receptionist = new Receptionist(controller.getNextId(), "Phoebe", "Buffay", Gender.FEMALE, LocalDate.of(1971, 8, 8), "6677889900", "phoebe", "phoebepass", 11, 2000.0, ProfessionalQualification.BASIC_SCHOOL);
        controller.addReceptionist(receptionist);

        Receptionist updatedReceptionist = new Receptionist(receptionist.getReceptionistId(), "Phoebe", "Buffay", Gender.FEMALE, LocalDate.of(1971, 8, 8), "6677889900", "phoebe", "newpass", 11, 2000.0, ProfessionalQualification.BASIC_SCHOOL);
        controller.updateReceptionist(updatedReceptionist);

        Receptionist modifiedReceptionist = controller.getReceptionistById(receptionist.getReceptionistId());
        assertNotNull(modifiedReceptionist);
        assertEquals("newpass", modifiedReceptionist.getPassword());
    }

    @Test
    public void testDisplayFreeRoomTypes() {
        // Test prikaza slobodnih tipova soba
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.displayFreeRoomTypes();
        
        String expectedOutput = "Free Room Types:\nRoom Type: DOUBLE_SINGLE_BED, Number of Beds: 2\n";
        String actualOutput = outContent.toString().replace("\r", "");
        
        System.err.println("Expected Output:");
        System.err.println(expectedOutput);
        System.err.println("Actual Output:");
        System.err.println(actualOutput);
        
        assertEquals(expectedOutput, actualOutput);

        System.setOut(System.out);
    }

    @Test
    public void testDisplayFreeRoomTypesByTimePeriod() {
        // Test prikaza slobodnih tipova soba u određenom periodu
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LocalDate checkInDate = LocalDate.of(2023, 7, 1);
        LocalDate checkOutDate = LocalDate.of(2023, 7, 10);
        controller.displayFreeRoomTypesByTimePeriod(checkInDate, checkOutDate);
        
        String expectedOutput = "Free Room Types:" + checkInDate + " - " + checkOutDate + "\nRoom Type: DOUBLE_SINGLE_BED, Number of Beds: 2\nRoom Type: SINGLE, Number of Beds: 1\n";
        String actualOutput = outContent.toString().replace("\r", "");
        
        System.err.println("Expected Output:");
        System.err.println(expectedOutput);
        System.err.println("Actual Output:");
        System.err.println(actualOutput);
        
        assertEquals(expectedOutput, actualOutput);

        System.setOut(System.out);
    }

}
