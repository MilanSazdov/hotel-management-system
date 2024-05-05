package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.Date;

public class Maid extends Staff {
	
	public Maid(String name, String lastName, Gender gender, LocalDate birthDate, String phoneNumber, String username, String password, int workingExperience, double salary, ProfessionalQualification professionalQualification) {
        super(name, lastName, gender, birthDate, phoneNumber, username, password, workingExperience, salary, professionalQualification);
    }
	
	public Maid() {
		super();
	}
	
	@Override
	public String toString() {
	    return "Maid{" + super.toString() + "}";
	}
}
