package com.hotelmanagement.controller;

import java.util.ArrayList;

import com.hotelmanagement.model.Maid;

public class MaidController {
	
	private static MaidController instance;
    private ArrayList<Maid> maidList;

    private MaidController() {
        maidList = new ArrayList<>();
    }

    public static synchronized MaidController getInstance() {
        if (instance == null) {
            instance = new MaidController();
        }
        return instance;
    }

    public void addMaid(Maid maid) {
        maidList.add(maid);
    }

    public void removeMaid(Maid maid) {
        maidList.remove(maid);
    }

    public void displayAllMaids() {
        for (Maid maid : maidList) {
            System.out.println(maid);
        }
    }

    public ArrayList<Maid> getMaidList() {
        return maidList;
    }
	
	public void setMaidList(ArrayList<Maid> maidList) {
		this.maidList = maidList;
	}
}
