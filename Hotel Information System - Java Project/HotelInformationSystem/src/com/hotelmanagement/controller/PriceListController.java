package com.hotelmanagement.controller;

import java.util.ArrayList;

import com.hotelmanagement.model.*;

public class PriceListController {
	
	private ArrayList<PriceList> allPriceList;
	private static PriceListController instance;
	
	private PriceListController() {
		allPriceList = new ArrayList<>();
	}
	
	public static synchronized PriceListController getInstance() {
		if (instance == null) {
			instance = new PriceListController();
		}
		return instance;
	}
	
	public void addPriceList(PriceList priceList) {
		allPriceList.add(priceList);
	}
	
	public void removePriceList(PriceList priceList) {
		allPriceList.remove(priceList);
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
	
	
	
}
