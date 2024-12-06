package com.hotelmanagement.test;

import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.model.AdditionalServices;
import java.io.*;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdditionalServicesControllerTest {

    private AdditionalServicesController controller;

    @BeforeEach
    public void setUp() {
        // Inicijalizuje instancu kontrolera i postavlja praznu listu dodatnih usluga
        controller = AdditionalServicesController.getInstance();
        controller.setAdditionalServicesList(new ArrayList<>());
    }

    @Test
    public void testAddAdditionalService() {
        // Test dodavanja dodatne usluge
        AdditionalServices service = new AdditionalServices(1, "Spa");
        controller.addAdditionalService(service);
        assertEquals(1, controller.getAdditionalServicesList().size());
        assertEquals(service, controller.getAdditionalServiceById(1));
    }

    @Test
    public void testRemoveAdditionalService() {
        // Test uklanjanja dodatne usluge
        AdditionalServices service = new AdditionalServices(2, "Gym");
        controller.addAdditionalService(service);
        controller.removeAdditionalService(service);
        assertEquals(0, controller.getAdditionalServicesList().size());
    }

    @Test
    public void testModifyAdditionalServiceName() {
        // Test izmene imena dodatne usluge
        AdditionalServices service = new AdditionalServices(3, "Pool");
        controller.addAdditionalService(service);
        controller.modifyAdditionalServiceName(3, "Swimming Pool");
        assertEquals("Swimming Pool", controller.getAdditionalServiceById(3).getServiceName());
    }

    @Test
    public void testGetAdditionalServiceById() {
        // Test preuzimanja dodatne usluge po ID-u
        AdditionalServices service = new AdditionalServices(4, "Sauna");
        controller.addAdditionalService(service);
        AdditionalServices retrievedService = controller.getAdditionalServiceById(4);
        assertNotNull(retrievedService);
        assertEquals(service, retrievedService);
    }

    @Test
    public void testUpdateAdditionalService() {
        // Test ažuriranja dodatne usluge
        AdditionalServices service = new AdditionalServices(5, "Massage");
        controller.addAdditionalService(service);
        AdditionalServices updatedService = new AdditionalServices(5, "Relaxing Massage");
        controller.updateAdditionalService(updatedService);
        assertEquals("Relaxing Massage", controller.getAdditionalServiceById(5).getServiceName());
    }

    @Test
    public void testLoadAdditionalServicesFromFile() {
        // Priprema privremenog CSV fajla za testiranje
        String testFilePath = "src/com/hotelmanagement/data_test/test_additional_services.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(testFilePath))) {
            bw.write("6,Room Service");
            bw.newLine();
            bw.write("7,Concierge");
        } catch (IOException e) {
            fail("Failed to set up test file: " + e.getMessage());
        }

        // Učitavanje usluga iz test fajla
        controller.loadAdditionalServicesFromFile(testFilePath); // Proveri da li je ova putanja ista kao u kontroleru
        assertEquals(2, controller.getAdditionalServicesList().size());
        assertEquals("Room Service", controller.getAdditionalServiceById(6).getServiceName());
        assertEquals("Concierge", controller.getAdditionalServiceById(7).getServiceName());

        // Brisanje test fajla
        new File(testFilePath).delete();
    }

    @Test
    public void testSaveAdditionalServicesToFile() {
        // Test čuvanja dodatnih usluga u fajl
        AdditionalServices service = new AdditionalServices(8, "Laundry");
        controller.addAdditionalService(service);

        String testFilePath = "src/com/hotelmanagement/data_test/test_save_additional_services.csv";
        controller.saveAdditionalServicesToFile(testFilePath);

        try (BufferedReader br = new BufferedReader(new FileReader(testFilePath))) {
            String line = br.readLine();
            assertNotNull(line);
            assertEquals("8,Laundry", line);
        } catch (IOException e) {
            fail("Error reading saved file: " + e.getMessage());
        }

        // Brisanje test fajla
        new File(testFilePath).delete();
    }

    @Test
    public void testDisplayAllAdditionalServices() {
        // Test prikaza svih dodatnih usluga
        AdditionalServices service1 = new AdditionalServices(9, "WiFi");
        AdditionalServices service2 = new AdditionalServices(10, "Breakfast");
        controller.addAdditionalService(service1);
        controller.addAdditionalService(service2);

        controller.displayAllAdditionalServices();  // Proveri izlaz u konzoli za ispravnost prikaza
    }

    @Test
    public void testDisplayAdditionalServiceByName() {
        // Test prikaza dodatne usluge po imenu
        AdditionalServices service = new AdditionalServices(11, "Parking");
        controller.addAdditionalService(service);
        controller.displayAdditionalServiceByName("Parking");  // Proveri izlaz u konzoli za ispravnost prikaza
    }

    @Test
    public void testSearchAdditionalServiceById() {
        // Test pretrage dodatne usluge po ID-u
        AdditionalServices service = new AdditionalServices(12, "Mini Bar");
        controller.addAdditionalService(service);
        controller.searchAdditionalServiceById(12);  // Proveri izlaz u konzoli za ispravnost prikaza
    }
}
