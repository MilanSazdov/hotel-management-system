package com.hotelmanagement.test;

import com.hotelmanagement.controller.*;
import com.hotelmanagement.model.Admin;
import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.Maid;
import com.hotelmanagement.model.Receptionist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataControllerTest {

    private DataController controller;

    @BeforeEach
    public void setUp() {
        // Inicijalizacija instance kontrolera
        controller = DataController.getInstance();
    }

    @Test
    public void testSetCurrentUserId() {
        // Test postavljanja trenutnog korisničkog ID-a
        controller.setCurrentUserId(10);
        assertEquals(10, controller.getCurrentUserId());
    }

    @Test
    public void testLoadData() {
        // Test učitavanja podataka
        controller.loadData();
        assertFalse(AdminController.getInstance().getAdminList().isEmpty());
        assertFalse(MaidController.getInstance().getAllMaids().isEmpty());
        assertFalse(ReceptionistController.getInstance().getReceptionistList().isEmpty());
        assertFalse(GuestController.getInstance().getGuestList().isEmpty());
    }

    @Test
    public void testPrintPriceLists() {
        // Test prikaza svih cenovnika (potrebno proveriti konzolni izlaz)
        controller.printPriceLists();
    }

    @Test
    public void testPrintAllUsers() {
        // Test prikaza svih korisnika (potrebno proveriti konzolni izlaz)
        controller.printAllUsers();
    }

    @Test
    public void testGetAllAdmins() {
        // Test preuzimanja svih admina
        assertFalse(controller.getAllAdmins().isEmpty());
    }

    @Test
    public void testGetAllMaids() {
        // Test preuzimanja svih sobarica
        assertFalse(controller.getAllMaids().isEmpty());
    }

    @Test
    public void testGetAllReceptionists() {
        // Test preuzimanja svih recepcionera
        assertFalse(controller.getAllReceptionists().isEmpty());
    }

    @Test
    public void testGetAllGuests() {
        // Test preuzimanja svih gostiju
        assertFalse(controller.getAllGuests().isEmpty());
    }

    @Test
    public void testLoadUser() {
        // Test učitavanja korisnika (potrebno proveriti konzolni izlaz)
        controller.loadUser("admin", "password123");
        controller.loadUser("nonexistent", "user");
    }

    @Test
    public void testAuthenticateUser() {
        // Test autentifikacije korisnika
        String role = controller.authenticateUser("pera_peric", "sifra123");
        assertNotNull(role);
        assertEquals("Admin", role);

        role = controller.authenticateUser("nonexistent", "user");
        assertNull(role);
        assertEquals(-1, controller.getCurrentUserId());
    }


    @Test
    public void testPrintAllRoomTypes() {
        // Test prikaza svih tipova soba (potrebno proveriti konzolni izlaz)
        controller.printAllRoomTypes();
    }

    @Test
    public void testPrintAllRooms() {
        // Test prikaza svih soba (potrebno proveriti konzolni izlaz)
        controller.printAllRooms();
    }

    @Test
    public void testPrintAllAdditionalServices() {
        // Test prikaza svih dodatnih usluga (potrebno proveriti konzolni izlaz)
        controller.printAllAdditionalServices();
    }
}
