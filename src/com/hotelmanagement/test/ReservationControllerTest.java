package com.hotelmanagement.test;

import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationControllerTest {

    private ReservationController controller;

    @BeforeEach
    public void setUp() {
        // Inicijalizacija instance kontrolera
        controller = ReservationController.getInstance();

        // Dodavanje test podataka
        controller.getAllReservations().clear(); // Očisti prethodne podatke

        Reservation reservation = new Reservation(1, 1, 1, 1, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 10), ReservationStatus.WAITING, new ArrayList<>(), 100.0);
        controller.addReservation(reservation);
    }

    @Test
    public void testAddReservation() {
        // Test dodavanja rezervacije
        Reservation reservation = new Reservation(2, 2, 2, 2, LocalDate.of(2023, 2, 1), LocalDate.of(2023, 2, 10), ReservationStatus.WAITING, new ArrayList<>(), 200.0);
        controller.addReservation(reservation);

        assertEquals(2, controller.getAllReservations().size());
        assertEquals(reservation, controller.findReservationById(2));
    }

    @Test
    public void testRemoveReservation() {
        // Test uklanjanja rezervacije
        Reservation reservation = new Reservation(2, 2, 2, 2, LocalDate.of(2023, 2, 1), LocalDate.of(2023, 2, 10), ReservationStatus.WAITING, new ArrayList<>(), 200.0);
        controller.addReservation(reservation);
        controller.removeReservation(reservation);

        assertEquals(1, controller.getAllReservations().size()); // Očekuje se 1 jer je jedna test rezervacija dodana u setUp
    }

    @Test
    public void testModifyReservationStatus() {
        // Test modifikacije statusa rezervacije
        controller.modifyReservationStatus(1, ReservationStatus.CONFIRMED);
        Reservation reservation = controller.findReservationById(1);
        assertNotNull(reservation);
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
    }

    @Test
    public void testModifyReservationCheckInDate() {
        // Test modifikacije datuma prijave
        LocalDate newCheckInDate = LocalDate.of(2023, 1, 5);
        controller.modifyReservationCheckInDate(1, newCheckInDate);
        Reservation reservation = controller.findReservationById(1);
        assertNotNull(reservation);
        assertEquals(newCheckInDate, reservation.getCheckInDate());
    }

    @Test
    public void testModifyReservationCheckOutDate() {
        // Test modifikacije datuma odjave
        LocalDate newCheckOutDate = LocalDate.of(2023, 1, 15);
        controller.modifyReservationCheckOutDate(1, newCheckOutDate);
        Reservation reservation = controller.findReservationById(1);
        assertNotNull(reservation);
        assertEquals(newCheckOutDate, reservation.getCheckOutDate());
    }

    @Test
    public void testGetReservationsByGuestId() {
        // Test preuzimanja rezervacija po ID-u gosta
        ArrayList<Reservation> reservations = controller.getReservationsByGuestId(1);
        assertEquals(1, reservations.size());
    }

    @Test
    public void testGetReservationsByStatus() {
        // Test preuzimanja rezervacija po statusu
        ArrayList<Reservation> reservations = controller.getReservationsByStatus(ReservationStatus.WAITING);
        assertEquals(1, reservations.size());
    }

    @Test
    public void testGetReservationsByGuestAndStatus() {
        // Test preuzimanja rezervacija po ID-u gosta i statusu
        ArrayList<Reservation> reservations = controller.getReservationsByGuestAndStatus(1, ReservationStatus.WAITING);
        assertEquals(1, reservations.size());
    }

    @Test
    public void testUpdateReservation() {
        // Test ažuriranja rezervacije
    	// Ovde se mora paziti zbog cenovnika, treba se pogledati da li je u fajlu dobar cenovnik
        Reservation updatedReservation = new Reservation(1, 1, 1, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 10), ReservationStatus.CONFIRMED, new ArrayList<>(), 150.0);
        controller.updateReservation(updatedReservation);
        
        // System.err.println(updatedReservation.getTotalCost());
        
        Reservation reservation = controller.findReservationById(1);
        assertNotNull(reservation);
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
        assertEquals(1000, reservation.getTotalCost());
    }

    @Test
    public void testGetReservationsInDateRange() {
        // Test preuzimanja rezervacija u određenom datumu
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 10);
        ArrayList<Reservation> reservations = controller.getReservationsInDateRange(startDate, endDate);
        assertEquals(1, reservations.size());
    }

    @Test
    public void testCancelReservation() {
        // Test otkazivanja rezervacije
        boolean isCancelled = controller.cancelReservation(1);
        assertTrue(isCancelled);
        assertEquals(ReservationStatus.CANCELLED, controller.findReservationById(1).getStatus());
    }

    @Test
    public void testDisplayAllReservations() {
        // Test displaying all reservations
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.displayAllReservations();

        String expectedOutput = "Reservation Id: 1, Room: null, Check In Date: 2023-01-01, Check Out Date: 2023-01-10, Status: WAITING, Guest Id: 1, Additional Services: No additional services, Total price: 100.00\n";
        String actualOutput = outContent.toString().trim().replace("\r\n", "\n").replace("\r", "\n");

        // Diagnostic output
        System.err.println("Expected Output:");
        System.err.println(expectedOutput);
        System.err.println("Actual Output:");
        System.err.println(actualOutput);

        assertEquals(expectedOutput.trim(), actualOutput);

        System.setOut(System.out);
    }

    @Test
    public void testDisplayReservationById() {
        // Test prikaza rezervacije po ID-u
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.displayReservationById(1);

        String expectedOutput = "Reservation Id: 1, Room: null, Check In Date: 2023-01-01, Check Out Date: 2023-01-10, Status: WAITING, Guest Id: 1, Additional Services: No additional services, Total price: 100.00\n";
        String actualOutput = outContent.toString().replace("\r", "").trim();

        // Dodavanje ispisa za dijagnostiku
        System.err.println("Expected Output:");
        System.err.println(expectedOutput);
        System.err.println("Actual Output:");
        System.err.println(actualOutput);

        assertEquals(expectedOutput.trim(), actualOutput);

        System.setOut(System.out);
    }


}
