package com.hotelmanagement.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hotelmanagement.model.*;

public class PriceListController {
	
	private ArrayList<PriceList> allPriceList;
	private static PriceListController instance;
	
	private PriceListController() {
		allPriceList = new ArrayList<>();
		loadPriceListsFromFile();
	}
	
	public static synchronized PriceListController getInstance() {
		if (instance == null) {
			instance = new PriceListController();
		}
		return instance;
	}
	
	public void addPriceList(PriceList priceList) {
		allPriceList.add(priceList);
		savePriceListsToFile();
	}
	
	public void removePriceList(PriceList priceList) {
		allPriceList.remove(priceList);
		savePriceListsToFile();
	}
	
	public void updatePriceList(PriceList priceList) {
        for (int i = 0; i < allPriceList.size(); i++) {
            if (allPriceList.get(i).getPriceListId() == priceList.getPriceListId()) {
                allPriceList.set(i, priceList);
                break;
            }
        }
        savePriceListsToFile();
    }
	
	public PriceList getPriceListById(int id) {
        for (PriceList priceList : allPriceList) {
            if (priceList.getPriceListId() == id) {
                return priceList;
            }
        }
        return null;
    }
	
	public void displayAllPriceList() {
		for (PriceList priceList : allPriceList) {
			System.out.println(priceList.toString());
		}
	}
	
	public void displayPriceListById(int id) {
		for (PriceList priceList : allPriceList) {
			if (priceList.getPriceListId() == id) {
				System.out.println(priceList.toString());
			}
		}
	}
	
	public ArrayList<PriceList> getAllPriceList() {
		return allPriceList;
	}
	
	public void setAllPriceList(ArrayList<PriceList> allPriceList) {
		this.allPriceList = allPriceList;
	}
	
	public void modifyPriceList(int id, PriceList priceList) {
		for (PriceList p : allPriceList) {
			if (p.getPriceListId() == id) {
				p = priceList;
			}
		}
	}
	
	public void searchPriceListById(int id) {
		for (PriceList priceList : allPriceList) {
			if (priceList.getPriceListId() == id) {
				System.out.println(priceList.toString());
			}
		}
	}
	
	public PriceList getApplicablePriceListForDate(LocalDate date) {
	    for (PriceList priceList : allPriceList) {
	        if (priceList.getValidFrom().compareTo(date) <= 0 && priceList.getValidTo().compareTo(date) >= 0) {
	            return priceList;
	        }
	    }
	    return null;
	}
	
	public void loadPriceListsFromFile() {
		String filePath = "src/com/hotelmanagement/data/price_list.csv";
	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] values = line.split(",");
	            int priceListId = Integer.parseInt(values[0]);
	            LocalDate validFrom = LocalDate.parse(values[1]);
	            LocalDate validTo = LocalDate.parse(values[2]);
	            Map<Integer, Double> roomTypePrices = parsePriceMap(values[3]);
	            Map<Integer, Double> additionalServicePrices = parsePriceMap(values[4]);
	            PriceList priceList = new PriceList(priceListId, validFrom, validTo, roomTypePrices, additionalServicePrices);
	            allPriceList.add(priceList);
	        }
	        System.out.println("Price lists loaded successfully.");
	    } catch (IOException e) {
	        System.out.println("Error reading from file: " + e.getMessage());
	    }
	}

	private Map<Integer, Double> parsePriceMap(String pricesString) {
	    if (pricesString.equals("{}")) {
	        return null;
	    }
	    Map<Integer, Double> priceMap = new HashMap<>();
	    String[] pairs = pricesString.substring(1, pricesString.length() - 1).split(";");
	    for (String pair : pairs) {
	        String[] keyValue = pair.split(":");
	        if (keyValue.length == 2) {
	            int key = Integer.parseInt(keyValue[0].trim());
	            double value = Double.parseDouble(keyValue[1].trim());
	            priceMap.put(key, value);
	        }
	    }
	    return priceMap;
	}


	 public void savePriceListsToFile() {
	        String filePath = "src/com/hotelmanagement/data/price_list.csv";
	        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) { // false to overwrite the file
	            for (PriceList priceList : allPriceList) {
	                bw.write(priceList.toCSVString());
	                bw.newLine();
	            }
	            System.out.println("Price lists saved successfully.");
	        } catch (IOException e) {
	            System.out.println("Error writing to file: " + e.getMessage());
	        }
	    }


    
    // Check if a date range overlaps with any existing price list
    public boolean isDateRangeOverlapping(LocalDate newStart, LocalDate newEnd) {
        for (PriceList existingPriceList : allPriceList) {
            if (existingPriceList.getValidFrom().isBefore(newEnd) && existingPriceList.getValidTo().isAfter(newStart)) {
                return true; // There is an overlap
            }
        }
        return false; // No overlap found
    }

	
}
