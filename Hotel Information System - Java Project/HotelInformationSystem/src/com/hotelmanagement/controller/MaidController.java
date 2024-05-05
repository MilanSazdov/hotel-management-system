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
	
	public void modifyMaid(int id, Maid maid) {
		for (Maid m : maidList) {
			if (m.getStaffId() == id) {
				m = maid;
			}
		}
	}
	
	public void searchMaidByName(String name) {
		for (Maid maid : maidList) {
			if (maid.getName().equals(name)) {
				System.out.println(maid);
			}
		}
	}
	
	public void displayMaidById(int id) {
		for (Maid maid : maidList) {
			if (maid.getStaffId() == id) {
				System.out.println(maid);
			}
		}
	}
	
	public void searchMaidByUsername(String email) {
		for (Maid maid : maidList) {
			if (maid.getUsername().equals(email)) {
				System.out.println(maid);
			}
		}
	}
	
	public void searchMaidByPhoneNumber(String phoneNumber) {
		for (Maid maid : maidList) {
			if (maid.getPhoneNumber().equals(phoneNumber)) {
				System.out.println(maid);
			}
		}
	}
	
	public void searchMaidById(int id) {
		for (Maid maid : maidList) {
			if (maid.getStaffId() == id) {
				System.out.println(maid);
			}
		}
	}
}
