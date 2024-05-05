package com.hotelmanagement.controller;

import java.util.ArrayList;

import com.hotelmanagement.model.Admin;

public class AdminController {
	
	private ArrayList<Admin> adminList;
	
	public AdminController() {
		adminList = new ArrayList<Admin>();
	}
	
	public void addAdmin(Admin admin) {
		adminList.add(admin);
    }
	
	public void removeAdmin(Admin admin) {
		adminList.remove(admin);
	}
	
	public void displayAllAdmins() {
		for (Admin admin : adminList) {
			System.out.println(admin.toString());
		}
	}
	
	public void displayAdminById(int id) {
		for (Admin admin : adminList) {
			if (admin.getStaffId() == id) {
				System.out.println(admin.toString());
			}
		}
	}
	
	public void modifyAdmin(int id, Admin admin) {
		for (Admin a : adminList) {
			if (a.getStaffId() == id) {
				a = admin;
			}
		}
	}
	
	public ArrayList<Admin> getAdminList() {
		return this.adminList;
	}
	
	public void setAdminList(ArrayList<Admin> adminList) {
		this.adminList = adminList;
	}
	
	public void searchAdminByName(String name) {
        for (Admin admin : adminList) {
            if (admin.getName().equals(name)) {
                System.out.println(admin.toString());
            }
        }
    }
	
	public void searchAdminByUsername(String email) {
		for (Admin admin : adminList) {
			if (admin.getUsername().equals(email)) {
				System.out.println(admin.toString());
			}
		}
	}
	
	public void searchAdminByPhoneNumber(String number) {
		for (Admin admin : adminList) {
			if (admin.getPhoneNumber().equals(number)) {
				System.out.println(admin.toString());
			}
		}
	}
	
	public void searchAdminById(int id) {
		for (Admin admin : adminList) {
			if (admin.getStaffId() == id) {
				System.out.println(admin.toString());
			}
		}
	}
	
}
