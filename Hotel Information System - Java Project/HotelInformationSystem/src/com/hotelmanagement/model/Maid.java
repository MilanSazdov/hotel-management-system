package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.Date;

public class Maid extends Staff {
	
	private static int nextId = 0;
	int maidId;
	
	public Maid(int id, String name, String lastName, Gender gender, LocalDate birthDate, String phoneNumber, String username, String password, int workingExperience, double salary, ProfessionalQualification professionalQualification) {
        super(name, lastName, gender, birthDate, phoneNumber, username, password, workingExperience, salary, professionalQualification);
        this.maidId = id;
	}
	
	public Maid(String name, String lastName, Gender gender, LocalDate birthDate, String phoneNumber, String username, String password, int workingExperience, double salary, ProfessionalQualification professionalQualification) {
        super(name, lastName, gender, birthDate, phoneNumber, username, password, workingExperience, salary, professionalQualification);
        this.maidId = nextId++;
    }
	
	public Maid() {
		super();
		this.maidId = nextId++;
	}
	
	public int getMaidId() {
		return this.maidId;
	}
	
	public void setMaidId(int id) {
		this.maidId = id;
	}
	
	@Override
	public String toString() {
	    return "Maid{" + super.toString() + "}";
	}
}
