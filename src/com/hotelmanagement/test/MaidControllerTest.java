package com.hotelmanagement.test;

import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.model.Gender;
import com.hotelmanagement.model.Maid;
import com.hotelmanagement.model.ProfessionalQualification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MaidControllerTest {

    private MaidController controller;

    @BeforeEach
    public void setUp() {
        // Inicijalizacija instance kontrolera
        controller = MaidController.getInstance();

        // Dodavanje test podataka
        controller.getMaidList().clear(); // Očisti prethodne podatke

        Maid maid = new Maid(1, "Test", "User", Gender.FEMALE, LocalDate.of(1990, 1, 1), "1234567890", "testuser", "password", 5, 2000.0, ProfessionalQualification.HIGH_SCHOOL, new ArrayList<>());
        controller.addMaid(maid);
    }

    @Test
    public void testAddMaid() {
        // Test dodavanja sobarice
        Maid maid = new Maid(2, "Anna", "Smith", Gender.FEMALE, LocalDate.of(1985, 1, 1), "1234567890", "anna", "password", 5, 2000.0, ProfessionalQualification.HIGH_SCHOOL, new ArrayList<>());
        controller.addMaid(maid);

        assertEquals(2, controller.getMaidList().size());
        assertEquals(maid, controller.getMaidById(2));
    }

    @Test
    public void testRemoveMaid() {
        // Test uklanjanja sobarice
        Maid maid = new Maid(2, "Jane", "Doe", Gender.FEMALE, LocalDate.of(1990, 2, 2), "0987654321", "jane", "password123", 3, 1800.0, ProfessionalQualification.COLLEGE, new ArrayList<>());
        controller.addMaid(maid);
        controller.removeMaid(maid);

        assertEquals(1, controller.getMaidList().size()); // Očekuje se 1 jer je jedan test korisnik dodan u setUp
    }

    @Test
    public void testGetMaidByUsername() {
        // Test preuzimanja sobarice po korisničkom imenu
        Maid maid = new Maid(2, "Alice", "Johnson", Gender.FEMALE, LocalDate.of(1980, 3, 3), "1122334455", "alice", "password321", 10, 2500.0, ProfessionalQualification.BASIC_SCHOOL, new ArrayList<>());
        controller.addMaid(maid);

        Maid foundMaid = controller.getMaidByUsername("alice");
        assertNotNull(foundMaid);
        assertEquals(maid, foundMaid);
    }

    @Test
    public void testCheckUserExists() {
        // Test provere postojanja korisnika
        Maid maid = new Maid(2, "Eve", "White", Gender.FEMALE, LocalDate.of(1975, 4, 4), "2233445566", "eve", "mypassword", 8, 2200.0, ProfessionalQualification.HIGH_SCHOOL, new ArrayList<>());
        controller.addMaid(maid);

        boolean userExists = controller.checkUserExists("eve", "mypassword");
        assertTrue(userExists);

        boolean userDoesNotExist = controller.checkUserExists("eve", "wrongpassword");
        assertFalse(userDoesNotExist);
    }

    @Test
    public void testGetMaidById() {
        // Test preuzimanja sobarice po ID-u
        Maid maid = new Maid(2, "Linda", "Brown", Gender.FEMALE, LocalDate.of(1988, 5, 5), "3344556677", "linda", "securepass", 6, 2100.0, ProfessionalQualification.COLLEGE, new ArrayList<>());
        controller.addMaid(maid);

        Maid foundMaid = controller.getMaidById(2);
        assertNotNull(foundMaid);
        assertEquals(maid, foundMaid);
    }

    @Test
    public void testGetNextId() {
        // Test preuzimanja sledećeg ID-a
        int nextId = controller.getNextId();
        assertEquals(2, nextId); // Očekuje se da je sledeći ID 2 jer je jedan test korisnik dodan u setUp
    }

    @Test
    public void testLoadMaidsFromFile() {
        // Test učitavanja sobarica iz CSV fajla
        controller.loadMaidsFromFile();
        assertFalse(controller.getMaidList().isEmpty());
    }

    @Test
    public void testSaveMaidsToFile() {
        // Test čuvanja sobarica u CSV fajl
        Maid maid = new Maid(2, "Monica", "Geller", Gender.FEMALE, LocalDate.of(1970, 7, 7), "5566778899", "monica", "monicapass", 12, 2400.0, ProfessionalQualification.COLLEGE, new ArrayList<>());
        controller.addMaid(maid);

        controller.saveMaidsToFile();
        // Provera sadržaja fajla može biti dodata ovde
    }

    @Test
    public void testUpdateMaid() {
        // Test ažuriranja sobarice
        Maid maid = new Maid(2, "Phoebe", "Buffay", Gender.FEMALE, LocalDate.of(1971, 8, 8), "6677889900", "phoebe", "phoebepass", 11, 2000.0, ProfessionalQualification.BASIC_SCHOOL, new ArrayList<>());
        controller.addMaid(maid);

        Maid updatedMaid = new Maid(2, "Phoebe", "Buffay", Gender.FEMALE, LocalDate.of(1971, 8, 8), "6677889900", "phoebe", "newpass", 11, 2000.0, ProfessionalQualification.BASIC_SCHOOL, new ArrayList<>());
        controller.updateMaid(updatedMaid);

        Maid modifiedMaid = controller.getMaidById(2);
        assertNotNull(modifiedMaid);
        assertEquals("newpass", modifiedMaid.getPassword());
    }
}
