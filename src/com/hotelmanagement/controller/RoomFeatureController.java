package com.hotelmanagement.controller;

import com.hotelmanagement.model.RoomFeature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RoomFeatureController {
    private static RoomFeatureController instance;
    private Map<Integer, Map<Integer, EnumSet<RoomFeature>>> roomFeatures;

    private RoomFeatureController() {
        roomFeatures = new HashMap<>();
        loadRoomFeatures("src/com/hotelmanagement/data/room_features.csv");
    }

    public static RoomFeatureController getInstance() {
        if (instance == null) {
            instance = new RoomFeatureController();
        }
        return instance;
    }

    private void loadRoomFeatures(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int roomId = Integer.parseInt(parts[0]);
                int roomTypeId = Integer.parseInt(parts[1]);
                EnumSet<RoomFeature> features = EnumSet.noneOf(RoomFeature.class);

                for (int i = 2; i < parts.length; i++) {
                    features.add(RoomFeature.valueOf(parts[i]));
                }

                roomFeatures.computeIfAbsent(roomTypeId, k -> new HashMap<>()).put(roomId, features);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getRoomIdsForTypeWithFeatures(int roomTypeId, EnumSet<RoomFeature> requiredFeatures) {
        if (!roomFeatures.containsKey(roomTypeId)) {
            return Collections.emptyList();
        }

        return roomFeatures.get(roomTypeId).entrySet().stream()
                .filter(entry -> entry.getValue().containsAll(requiredFeatures))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Integer> getAllRoomIdsForType(int roomTypeId) {
        if (!roomFeatures.containsKey(roomTypeId)) {
            return Collections.emptyList();
        }

        return new ArrayList<>(roomFeatures.get(roomTypeId).keySet());
    }

    public EnumSet<RoomFeature> getFeaturesForRoom(int roomTypeId, int roomId) {
        if (!roomFeatures.containsKey(roomTypeId)) {
            return EnumSet.noneOf(RoomFeature.class);
        }

        return roomFeatures.get(roomTypeId).getOrDefault(roomId, EnumSet.noneOf(RoomFeature.class));
    }

    public void saveRoomFeatures(int roomTypeId, int roomId, EnumSet<RoomFeature> features) {
        roomFeatures.computeIfAbsent(roomTypeId, k -> new HashMap<>()).put(roomId, features);
        saveFeaturesToFile();
    }

    public void removeRoomFeatures(int roomId) {
        for (Map<Integer, EnumSet<RoomFeature>> typeFeatures : roomFeatures.values()) {
            typeFeatures.remove(roomId);
        }
        saveFeaturesToFile();
    }

    private void saveFeaturesToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/com/hotelmanagement/data/room_features.csv"))) {
            for (Map.Entry<Integer, Map<Integer, EnumSet<RoomFeature>>> typeEntry : roomFeatures.entrySet()) {
                int roomTypeId = typeEntry.getKey();
                for (Map.Entry<Integer, EnumSet<RoomFeature>> roomEntry : typeEntry.getValue().entrySet()) {
                    int roomId = roomEntry.getKey();
                    String features = roomEntry.getValue().stream()
                            .map(RoomFeature::name)
                            .collect(Collectors.joining(","));
                    bw.write(roomId + "," + roomTypeId + "," + features);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<Integer> getRoomIdsForTypeWithFeature(int roomTypeId, RoomFeature requiredFeature) {
        if (!roomFeatures.containsKey(roomTypeId)) {
            return Collections.emptyList();
        }

        return roomFeatures.get(roomTypeId).entrySet().stream()
                .filter(entry -> entry.getValue().contains(requiredFeature))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
