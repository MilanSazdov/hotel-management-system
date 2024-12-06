package com.hotelmanagement.test;

import com.hotelmanagement.controller.PriceListController;
import com.hotelmanagement.model.PriceList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PriceListControllerTest {

    private PriceListController controller;

    @BeforeEach
    public void setUp() {
        // Inicijalizacija instance kontrolera i postavljanje prazne liste cenovnika
        controller = PriceListController.getInstance();
        controller.getAllPriceList().clear();
    }

    @Test
    public void testAddPriceList() {
        // Test dodavanja cenovnika
        Map<Integer, Double> roomTypePrices = new HashMap<>();
        Map<Integer, Double> additionalServicePrices = new HashMap<>();
        PriceList priceList = new PriceList(1, LocalDate.now(), LocalDate.now().plusDays(10), roomTypePrices, additionalServicePrices);
        controller.addPriceList(priceList);
        
        assertEquals(1, controller.getAllPriceList().size());
        assertEquals(priceList, controller.getPriceListById(1));
    }

    @Test
    public void testRemovePriceList() {
        // Test uklanjanja cenovnika
        Map<Integer, Double> roomTypePrices = new HashMap<>();
        Map<Integer, Double> additionalServicePrices = new HashMap<>();
        PriceList priceList = new PriceList(2, LocalDate.now(), LocalDate.now().plusDays(10), roomTypePrices, additionalServicePrices);
        controller.addPriceList(priceList);
        controller.removePriceList(priceList);
        
        assertEquals(0, controller.getAllPriceList().size());
    }

    @Test
    public void testUpdatePriceList() {
        // Test ažuriranja cenovnika
        Map<Integer, Double> roomTypePrices = new HashMap<>();
        Map<Integer, Double> additionalServicePrices = new HashMap<>();
        PriceList priceList = new PriceList(3, LocalDate.now(), LocalDate.now().plusDays(10), roomTypePrices, additionalServicePrices);
        controller.addPriceList(priceList);
        
        PriceList updatedPriceList = new PriceList(3, LocalDate.now(), LocalDate.now().plusDays(20), roomTypePrices, additionalServicePrices);
        controller.updatePriceList(updatedPriceList);
        
        assertEquals(updatedPriceList, controller.getPriceListById(3));
    }

    @Test
    public void testGetPriceListById() {
        // Test preuzimanja cenovnika po ID-u
        Map<Integer, Double> roomTypePrices = new HashMap<>();
        Map<Integer, Double> additionalServicePrices = new HashMap<>();
        PriceList priceList = new PriceList(4, LocalDate.now(), LocalDate.now().plusDays(10), roomTypePrices, additionalServicePrices);
        controller.addPriceList(priceList);
        
        PriceList foundPriceList = controller.getPriceListById(4);
        assertNotNull(foundPriceList);
        assertEquals(priceList, foundPriceList);
    }

    @Test
    public void testGetApplicablePriceListForDate() {
        // Test preuzimanja cenovnika za određeni datum
        Map<Integer, Double> roomTypePrices = new HashMap<>();
        Map<Integer, Double> additionalServicePrices = new HashMap<>();
        PriceList priceList = new PriceList(5, LocalDate.now(), LocalDate.now().plusDays(10), roomTypePrices, additionalServicePrices);
        controller.addPriceList(priceList);
        
        PriceList applicablePriceList = controller.getApplicablePriceListForDate(LocalDate.now().plusDays(5));
        assertNotNull(applicablePriceList);
        assertEquals(priceList, applicablePriceList);
    }

    @Test
    public void testLoadPriceListsFromFile() {
        // Test učitavanja cenovnika iz CSV fajla
        controller.loadPriceListsFromFile();
        assertFalse(controller.getAllPriceList().isEmpty());
    }

    @Test
    public void testSavePriceListsToFile() {
        // Test čuvanja cenovnika u CSV fajl
        Map<Integer, Double> roomTypePrices = new HashMap<>();
        Map<Integer, Double> additionalServicePrices = new HashMap<>();
        PriceList priceList = new PriceList(6, LocalDate.now(), LocalDate.now().plusDays(10), roomTypePrices, additionalServicePrices);
        controller.addPriceList(priceList);
        
        controller.savePriceListsToFile();
        // Provera sadržaja fajla može biti dodata ovde
    }

    @Test
    public void testIsDateRangeOverlapping() {
        // Test provere preklapanja perioda datuma
        Map<Integer, Double> roomTypePrices = new HashMap<>();
        Map<Integer, Double> additionalServicePrices = new HashMap<>();
        PriceList priceList1 = new PriceList(7, LocalDate.now(), LocalDate.now().plusDays(10), roomTypePrices, additionalServicePrices);
        PriceList priceList2 = new PriceList(8, LocalDate.now().plusDays(5), LocalDate.now().plusDays(15), roomTypePrices, additionalServicePrices);
        controller.addPriceList(priceList1);
        
        boolean isOverlapping = controller.isDateRangeOverlapping(LocalDate.now().plusDays(5), LocalDate.now().plusDays(15));
        assertTrue(isOverlapping);
        
        boolean isNotOverlapping = controller.isDateRangeOverlapping(LocalDate.now().plusDays(11), LocalDate.now().plusDays(20));
        assertFalse(isNotOverlapping);
    }
}
