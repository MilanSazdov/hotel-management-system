package com.hotelmanagement.test;

import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.model.Gender;
import com.hotelmanagement.model.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GuestControllerTest {

    private GuestController controller;

    @BeforeEach
    public void setUp() {
        // Inicijalizacija instance kontrolera i postavljanje prazne liste gostiju
        controller = GuestController.getInstance();
        controller.getGuestList().clear();
    }

    @Test
    public void testAddGuest() {
        // Test dodavanja gosta
        Guest guest = new Guest(1, "John", "Doe", Gender.MALE, LocalDate.of(1990, 1, 1), "123456789", "john.doe@example.com", "AB123456", null);
        controller.addGuest(guest);

        assertEquals(1, controller.getGuestList().size());
        assertEquals(guest, controller.getGuestById(1));
    }

    @Test
    public void testRemoveGuest() {
        // Test uklanjanja gosta
        Guest guest = new Guest(2, "Jane", "Doe", Gender.FEMALE, LocalDate.of(1995, 2, 2), "987654321", "jane.doe@example.com", "CD789012", null);
        controller.addGuest(guest);
        controller.removeGuest(guest);

        assertEquals(0, controller.getGuestList().size());
    }

    @Test
    public void testGetGuestById() {
        // Test preuzimanja gosta po ID-u
        Guest guest = new Guest(3, "Alice", "Smith", Gender.FEMALE, LocalDate.of(1985, 3, 3), "111222333", "alice.smith@example.com", "EF345678", null);
        controller.addGuest(guest);

        Guest foundGuest = controller.getGuestById(3);
        assertNotNull(foundGuest);
        assertEquals(guest, foundGuest);
    }

    @Test
    public void testGetGuestByEmail() {
        // Test preuzimanja gosta po email-u
        Guest guest = new Guest(4, "Bob", "Brown", Gender.MALE, LocalDate.of(1980, 4, 4), "444555666", "bob.brown@example.com", "GH901234", null);
        controller.addGuest(guest);

        Guest foundGuest = controller.getGuestByEmail("bob.brown@example.com");
        assertNotNull(foundGuest);
        assertEquals(guest, foundGuest);
    }

    @Test
    public void testSaveGuestsToFile() {
        // Test čuvanja gostiju u CSV fajl
        Guest guest = new Guest(5, "Charlie", "Davis", Gender.MALE, LocalDate.of(1975, 5, 5), "777888999", "charlie.davis@example.com", "IJ567890", null);
        controller.addGuest(guest);

        controller.saveGuestsToFile();
        // Provera sadržaja fajla može biti dodata ovde
    }

    @Test
    public void testLoadGuestsFromFile() {
        // Test učitavanja gostiju iz CSV fajla
        controller.loadGuestsFromFile();
        assertFalse(controller.getGuestList().isEmpty());
    }

    @Test
    public void testAppendGuestToFile() {
        // Test dodavanja gosta u CSV fajl
        Guest guest = new Guest(6, "Diana", "Evans", Gender.FEMALE, LocalDate.of(1992, 6, 6), "101010101", "diana.evans@example.com", "KL678901", null);
        controller.appendGuestToFile(guest);
        // Provera sadržaja fajla može biti dodata ovde
    }

    @Test
    public void testSearchGuestByName() {
        // Test pretrage gosta po imenu
        Guest guest = new Guest(7, "Emily", "Green", Gender.FEMALE, LocalDate.of(1991, 7, 7), "121212121", "emily.green@example.com", "MN345678", null);
        controller.addGuest(guest);

        // Provera konzolnog izlaza može biti dodata ovde
        controller.searchGuestByName("Emily");
    }

    @Test
    public void testCheckUserExists() {
        // Test provere postojanja korisnika
        Guest guest = new Guest(8, "Frank", "Harris", Gender.MALE, LocalDate.of(1988, 8, 8), "131313131", "frank.harris@example.com", "OP123456", null);
        guest.setUsername("frankh");
        guest.setPassword("password123");
        controller.addGuest(guest);

        boolean userExists = controller.checkUserExists("frankh", "password123");
        assertTrue(userExists);

        boolean userDoesNotExist = controller.checkUserExists("frankh", "wrongpassword");
        assertFalse(userDoesNotExist);
    }
}
