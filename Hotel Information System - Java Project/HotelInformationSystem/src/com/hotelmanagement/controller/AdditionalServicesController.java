package com.hotelmanagement.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.hotelmanagement.model.AdditionalServices;

public class AdditionalServicesController {
    
    private static AdditionalServicesController instance;
    private ArrayList<AdditionalServices> additionalServicesList;

    private AdditionalServicesController() {
        additionalServicesList = new ArrayList<>();
    }

    public static synchronized AdditionalServicesController getInstance() {
        if (instance == null) {
            instance = new AdditionalServicesController();
        }
        return instance;
    }

    public void addAdditionalService(AdditionalServices additionalService) {
        additionalServicesList.add(additionalService);
    }

    public void removeAdditionalService(AdditionalServices additionalService) {
        additionalServicesList.remove(additionalService);
    }

    public void displayAllAdditionalServices() {
        for (AdditionalServices additionalService : additionalServicesList) {
            System.out.println(additionalService.toString());
        }
    }
    
    public void displayAdditionalServiceByName(String name) {
        for (AdditionalServices additionalService : additionalServicesList) {
            if (additionalService.getServiceName().equals(name)) {
                System.out.println(additionalService.toString());
            }
        }
    }
    
    public ArrayList<AdditionalServices> getAdditionalServicesList() {
        return additionalServicesList;
    }
    
    public void setAdditionalServicesList(ArrayList<AdditionalServices> additionalServicesList) {
        this.additionalServicesList = additionalServicesList;
    }
    
    public void searchAdditionalServiceById(int id) {
        for (AdditionalServices additionalService : additionalServicesList) {
            if (additionalService.getServiceId() == id) {
                System.out.println(additionalService.toString());
            }
        }
    }
    
    public void loadAdditionalServicesFromFile() {
        String path = "src/com/hotelmanagement/data/additional_services.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int serviceId = Integer.parseInt(values[0]);
                String serviceName = values[1];

                AdditionalServices additionalService = new AdditionalServices(serviceId, serviceName);
                additionalServicesList.add(additionalService);
            }
            System.out.println("Additional services loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }
    
    public void saveAdditionalServicesToFile() {
        String path = "src/com/hotelmanagement/data/additional_services.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (AdditionalServices additionalService : additionalServicesList) {
                bw.write(additionalService.toCSVString());
                bw.newLine();
            }
            System.out.println("Additional services saved successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
