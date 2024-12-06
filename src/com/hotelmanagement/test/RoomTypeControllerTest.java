package com.hotelmanagement.test;

import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.model.RoomCategory;
import com.hotelmanagement.model.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTypeControllerTest {

    private RoomTypeController controller;

    @BeforeEach
    public void setUp() {
        // Inicijalizuje instancu kontrolera i postavlja praznu listu tipova soba
        controller = RoomTypeController.getInstance();
        controller.setRoomTypeList(new ArrayList<>());
    }

    @Test
    public void testAddRoomType() {
        // Test dodavanja tipa sobe
        RoomType roomType = new RoomType(1, 2, RoomCategory.DOUBLE_SINGLE_BED);
        controller.addRoomType(roomType);
        assertEquals(1, controller.getRoomTypeList().size());
        assertEquals(roomType, controller.searchRoomTypeById(1));
    }

    @Test
    public void testRemoveRoomType() {
        // Test uklanjanja tipa sobe
        RoomType roomType = new RoomType(2, 1, RoomCategory.DELUXE);
        controller.addRoomType(roomType);
        controller.removeRoomType(roomType);
        assertEquals(0, controller.getRoomTypeList().size());
    }

    @Test
    public void testModifyRoomTypeCategory() {
        // Test izmene kategorije tipa sobe
        RoomType roomType = new RoomType(3, 3, RoomCategory.SINGLE);
        controller.addRoomType(roomType);
        controller.modifyRoomTypeCategory(3, RoomCategory.KING);
        assertEquals(RoomCategory.KING, controller.searchRoomTypeById(3).getCategory());
    }

    @Test
    public void testSearchRoomTypeById() {
        // Test pretrage tipa sobe po ID-u
        RoomType roomType = new RoomType(4, 2, RoomCategory.QUEEN);
        controller.addRoomType(roomType);
        RoomType foundRoomType = controller.searchRoomTypeById(4);
        assertNotNull(foundRoomType);
        assertEquals(roomType, foundRoomType);
    }

    @Test
    public void testSearchRoomTypeByCategory() {
        // Test pretrage tipa sobe po kategoriji
        RoomType roomType1 = new RoomType(5, 2, RoomCategory.DOUBLE_SINGLE_BED);
        RoomType roomType2 = new RoomType(6, 1, RoomCategory.DOUBLE_TWIN_BEDS);
        controller.addRoomType(roomType1);
        controller.addRoomType(roomType2);
        
        // Check console output for category match
        controller.searchRoomTypeByCategory(RoomCategory.DOUBLE_SINGLE_BED);
        // You may want to assert the console output if using a custom logger
    }

    @Test
    public void testLoadRoomTypesFromFile() {
        // Priprema privremenog CSV fajla za testiranje
        String testFilePath = "src/com/hotelmanagement/data_test/test_roomtypes.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(testFilePath))) {
            bw.write("7,2,SINGLE");
            bw.newLine();
            bw.write("8,3,DELUXE");
        } catch (IOException e) {
            fail("Failed to set up test file: " + e.getMessage());
        }

        // Učitavanje tipova soba iz test fajla
        controller.loadRoomTypesFromFile(testFilePath);
        assertEquals(2, controller.getRoomTypeList().size());
        assertEquals(RoomCategory.SINGLE, controller.searchRoomTypeById(7).getCategory());
        assertEquals(RoomCategory.DELUXE, controller.searchRoomTypeById(8).getCategory());

        // Brisanje test fajla
        new File(testFilePath).delete();
    }

    @Test
    public void testSaveRoomTypesToFile() {
        // Test čuvanja tipova soba u fajl
        RoomType roomType = new RoomType(9, 4, RoomCategory.KING);
        controller.addRoomType(roomType);

        String testFilePath = "src/com/hotelmanagement/data_test/test_save_roomtypes.csv";
        controller.saveRoomTypesToFile(testFilePath);

        try (BufferedReader br = new BufferedReader(new FileReader(testFilePath))) {
            String line = br.readLine();
            assertNotNull(line);
            assertEquals("9,4,KING", line);
        } catch (IOException e) {
            fail("Error reading saved file: " + e.getMessage());
        }

        // Brisanje test fajla
        new File(testFilePath).delete();
    }

    @Test
    public void testDisplayAllRoomTypes() {
        // Test prikaza svih tipova soba
        RoomType roomType1 = new RoomType(10, 2, RoomCategory.TRIPLE);
        RoomType roomType2 = new RoomType(11, 3, RoomCategory.QUAD);
        controller.addRoomType(roomType1);
        controller.addRoomType(roomType2);

        controller.displayAllRoomTypes();  // Proveri izlaz u konzoli za ispravnost prikaza
    }
}
