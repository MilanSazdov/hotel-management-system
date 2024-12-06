package com.hotelmanagement.test;

import com.hotelmanagement.controller.AdminController;
import com.hotelmanagement.model.Admin;
import com.hotelmanagement.model.Gender;
import com.hotelmanagement.model.ProfessionalQualification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AdminControllerTest {

    private AdminController controller;

    @BeforeEach
    public void setUp() {
        // Inicijalizacija instance kontrolera
        controller = AdminController.getInstance();

        // Dodavanje test podataka
        controller.getAdminList().clear(); // Očisti prethodne podatke

        Admin admin = new Admin(1, "Test", "User", Gender.MALE, LocalDate.of(1990, 1, 1), "1234567890", "testuser", "password", 5, 2000.0, ProfessionalQualification.HIGH_SCHOOL);
        controller.addAdmin(admin);
    }

    @Test
    public void testAddAdmin() {
        // Test dodavanja admina
        Admin admin = new Admin(2, "Anna", "Smith", Gender.FEMALE, LocalDate.of(1985, 1, 1), "1234567890", "anna", "password", 5, 2000.0, ProfessionalQualification.HIGH_SCHOOL);
        controller.addAdmin(admin);

        assertEquals(2, controller.getAdminList().size());
        assertEquals(admin, controller.getAdminByUsername("anna"));
    }

    @Test
    public void testRemoveAdmin() {
        // Test uklanjanja admina
        Admin admin = new Admin(2, "Jane", "Doe", Gender.FEMALE, LocalDate.of(1990, 2, 2), "0987654321", "jane", "password123", 3, 1800.0, ProfessionalQualification.COLLEGE);
        controller.addAdmin(admin);
        controller.removeAdmin(admin);

        assertEquals(1, controller.getAdminList().size()); // Očekuje se 1 jer je jedan test korisnik dodan u setUp
    }

    @Test
    public void testGetAdminByUsername() {
        // Test preuzimanja admina po korisničkom imenu
        Admin admin = new Admin(2, "Alice", "Johnson", Gender.FEMALE, LocalDate.of(1980, 3, 3), "1122334455", "alice", "password321", 10, 2500.0, ProfessionalQualification.BASIC_SCHOOL);
        controller.addAdmin(admin);

        Admin foundAdmin = controller.getAdminByUsername("alice");
        assertNotNull(foundAdmin);
        assertEquals(admin, foundAdmin);
    }

    @Test
    public void testCheckUserExists() {
        // Test provere postojanja korisnika
        Admin admin = new Admin(2, "Eve", "White", Gender.FEMALE, LocalDate.of(1975, 4, 4), "2233445566", "eve", "mypassword", 8, 2200.0, ProfessionalQualification.HIGH_SCHOOL);
        controller.addAdmin(admin);

        boolean userExists = controller.checkUserExists("eve", "mypassword");
        assertTrue(userExists);

        boolean userDoesNotExist = controller.checkUserExists("eve", "wrongpassword");
        assertFalse(userDoesNotExist);
    }

    @Test
    public void testGetAdminById() {
        // Test preuzimanja admina po ID-u
        Admin admin = new Admin(2, "Linda", "Brown", Gender.FEMALE, LocalDate.of(1988, 5, 5), "3344556677", "linda", "securepass", 6, 2100.0, ProfessionalQualification.COLLEGE);
        controller.addAdmin(admin);

        Admin foundAdmin = controller.getAdminByUsername("linda");
        assertNotNull(foundAdmin);
        assertEquals(admin, foundAdmin);
    }

    @Test
    public void testLoadAdminsFromFile() {
        // Test učitavanja admina iz CSV fajla
        controller.loadAdminsFromFile();
        assertFalse(controller.getAdminList().isEmpty());
    }

    @Test
    public void testSaveAdminsToFile() {
        // Test čuvanja admina u CSV fajl
        Admin admin = new Admin(2, "Monica", "Geller", Gender.FEMALE, LocalDate.of(1970, 7, 7), "5566778899", "monica", "monicapass", 12, 2400.0, ProfessionalQualification.COLLEGE);
        controller.addAdmin(admin);

        controller.saveAdminsToFile();
        // Provera sadržaja fajla može biti dodata ovde
    }

    @Test
    public void testUpdateAdmin() {
        // Test ažuriranja admina
        Admin admin = new Admin(2, "Phoebe", "Buffay", Gender.FEMALE, LocalDate.of(1971, 8, 8), "6677889900", "phoebe", "phoebepass", 11, 2000.0, ProfessionalQualification.BASIC_SCHOOL);
        controller.addAdmin(admin);

        Admin updatedAdmin = new Admin(2, "Phoebe", "Buffay", Gender.FEMALE, LocalDate.of(1971, 8, 8), "6677889900", "phoebe", "newpass", 11, 2000.0, ProfessionalQualification.BASIC_SCHOOL);
        controller.modifyAdmin(2, updatedAdmin);

        Admin modifiedAdmin = controller.getAdminByUsername("phoebe");
        assertNotNull(modifiedAdmin);
        assertEquals("newpass", modifiedAdmin.getPassword());
    }

    @Test
    public void testDisplayAllAdmins() {
        // Provera sadržaja liste admina pre poziva metode displayAllAdmins
        for (Admin admin : controller.getAdminList()) {
            System.err.println(admin.toString());
        }

        // Test prikaza svih admina
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.displayAllAdmins();

        // Formatiranje očekivanog izlaza u skladu sa toString metodom
        String expectedOutput = "Admin{id: 1Staff{name='Test', lastName='User', gender=MALE, birthDate=1990-01-01, phoneNumber='1234567890', username='testuser', password='password', workingExperience=5, salary=29250.00, professionalQualification='IV Step - High school'}}\n";

        String actualOutput = outContent.toString().replace("\r", "");

        // Dodavanje ispisa za dijagnostiku koristeći System.err
        System.err.println("Expected Output:");
        System.err.println(expectedOutput);
        System.err.println("Actual Output:");
        System.err.println(actualOutput);

        assertEquals(expectedOutput, actualOutput);

        System.setOut(System.out);
    }



}
