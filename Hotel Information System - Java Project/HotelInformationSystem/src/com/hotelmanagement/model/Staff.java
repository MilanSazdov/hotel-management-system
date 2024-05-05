package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.Date;

public class Staff extends User {
	
	private int workingExperience;
	private double salary;
	private ProfessionalQualification professionalQualification;
	private static int id = 0; // Promenljiva id sa kojom mozemo da pratimo i broj ljudi i id svakog od njih
	private int staffId;
	
	public Staff(String name, String lastName, Gender gender, LocalDate currentDate, String phoneNumber, String username, String password, int workingExperience, double salary, ProfessionalQualification professionalQualification){
        super(name, lastName, gender, currentDate, phoneNumber, username, password);
        this.workingExperience = workingExperience;
        this.salary = salary;
        this.professionalQualification = professionalQualification;
        this.staffId = ++id;
    }
	
	public Staff() {
		super();
		this.workingExperience = 0;
		this.salary = 0.0;
		this.professionalQualification = null;
		this.staffId = ++id;
	}
	
	public int getWorkingExperience() {
        return this.workingExperience;
    }

    public double getSalary() {
        return this.salary;
    }

    public ProfessionalQualification getProfessionalQualification() {
        return this.professionalQualification;
    }

    public static int getId() {
        return id;
    }
    
	public int getStaffId() {
		return this.staffId;
	}

    public void setWorkingExperience(int workingExperience) {
        this.workingExperience = workingExperience;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setProfessionalQualification(ProfessionalQualification professionalQualification) {
        this.professionalQualification = professionalQualification;
    }
	
    public void calculateSalary() {
        double baseSalary = 20000; // Pocetna plata koju svaki zaposleni ima
        double experienceIncrement = 500 * workingExperience; // Povecanje plate za svaku godinu radnog iskustva
        double qualificationMultiplier = getQualificationMultiplier(professionalQualification); // Multiplikator plate u zavisnosti od kvalifikacije
        
        this.salary = (baseSalary + experienceIncrement) * qualificationMultiplier;
    }

    private double getQualificationMultiplier(ProfessionalQualification qualification) {
        switch (qualification) {
            case PRIMARY_SCHOOL_FOURTH_GRADE:
                return 1.0;
            case BASIC_SCHOOL:
                return 1.1;
            case VOCATIONAL_HIGH_SCHOOL:
                return 1.2;
            case HIGH_SCHOOL:
                return 1.3;
            case HIGHER_EDUCATION_SHORT_PROGRAM:
                return 1.4;
            case COLLEGE:
                return 1.6;
            case BACHELOR:
            case BACHELOR_PROFESSIONAL:
                return 1.8;
            case SPECIALIST_PROFESSIONAL:
            case BACHELOR_ACADEMIC:
                return 2.0;
            case MASTER_INTEGRATED:
            case MASTER:
                return 2.2;
            case MASTER_OF_SCIENCE:
                return 2.4;
            case SPECIALIZATION_MEDICINE:
                return 2.6;
            case SPECIALIST_ACADEMIC:
                return 2.8;
            case DOCTORATE:
                return 3.0;
            default:
                return 1.0;
        }
    }
    
    @Override
    public String toString() {
        return "Staff{" +
               "name='" + name + '\'' +
               ", lastName='" + lastName + '\'' +
               ", gender=" + gender +
               ", birthDate=" + birthDate +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", workingExperience=" + workingExperience +
               ", salary=" + salary +
               ", professionalQualification=" + professionalQualification.getDescription() +
               ", id=" + id +
               '}';
    }

}
