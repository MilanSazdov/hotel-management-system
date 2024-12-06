package com.hotelmanagement.test;

import com.hotelmanagement.controller.RoomFeatureController;
import com.hotelmanagement.model.RoomFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomFeatureControllerTest {

    private RoomFeatureController controller;

    @BeforeEach
    public void setUp() {
        // Inicijalizacija instance kontrolera
        controller = RoomFeatureController.getInstance();
    }

    @Test
    public void testGetRoomIdsForTypeWithFeatures() {
        // Test preuzimanja ID-ova soba za određeni tip sa specifičnim karakteristikama
        EnumSet<RoomFeature> features = EnumSet.of(RoomFeature.AIR_CONDITIONING, RoomFeature.BALCONY);

        List<Integer> roomIds = controller.getRoomIdsForTypeWithFeatures(1, features);

        // Provera da li lista ID-ova soba nije prazna i sadrži očekivane ID-ove
        assertNotNull(roomIds);
        assertFalse(roomIds.isEmpty());
        System.out.println("Room IDs found: " + roomIds);
        assertTrue(roomIds.contains(1)); // Prilagodite prema podacima u CSV fajlu
    }


    @Test
    public void testGetRoomIdsForTypeWithNonExistingFeatures() {
        // Test preuzimanja ID-ova soba za tip sa karakteristikama koje ne postoje zajedno
        EnumSet<RoomFeature> features = EnumSet.of(RoomFeature.SMOKING, RoomFeature.NON_SMOKING);

        List<Integer> roomIds = controller.getRoomIdsForTypeWithFeatures(1, features);

        // Provera da li je lista prazna ako nijedna soba ne odgovara svim karakteristikama
        assertNotNull(roomIds);
        assertTrue(roomIds.isEmpty());
    }

    @Test
    public void testGetRoomIdsForNonExistingRoomType() {
        // Test preuzimanja ID-ova soba za nepostojeći tip sobe
        EnumSet<RoomFeature> features = EnumSet.of(RoomFeature.TV);

        List<Integer> roomIds = controller.getRoomIdsForTypeWithFeatures(99, features);

        // Provera da li je lista prazna za nepostojeći tip sobe
        assertNotNull(roomIds);
        assertTrue(roomIds.isEmpty());
    }
}
