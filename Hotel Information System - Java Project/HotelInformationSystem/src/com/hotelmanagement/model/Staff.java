package com.hotelmanagement.model;

import java.time.LocalDate;

public class Staff extends User {
    
    private int workingExperience;
    private double salary;
    private ProfessionalQualification professionalQualification;
    
    public Staff(String name, String lastName, Gender gender, LocalDate birthDate, String phoneNumber, String username, String password, int workingExperience, double salary, ProfessionalQualification professionalQualification) {
        super(name, lastName, gender, birthDate, phoneNumber, username, password);
        this.workingExperience = workingExperience;
        this.professionalQualification = professionalQualification;
        calculateSalary(); // Ensure salary is calculated after all fields are set
    }
    
    public Staff() {
        super();
        this.workingExperience = 0;
        this.salary = 0.0;
        this.professionalQualification = ProfessionalQualification.BASIC_SCHOOL; // Default value or consider null handling
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

    public void setWorkingExperience(int workingExperience) {
        this.workingExperience = workingExperience;
        calculateSalary(); // Recalculate salary when experience changes
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setProfessionalQualification(ProfessionalQualification professionalQualification) {
        this.professionalQualification = professionalQualification;
        calculateSalary(); // Recalculate salary when qualification changes
    }
    
    public void calculateSalary() {
        double baseSalary = 20000; // Base salary which every employee has
        double experienceIncrement = 500 * workingExperience; // Salary increase for each year of experience
        double qualificationMultiplier = (professionalQualification != null) ? getQualificationMultiplier(professionalQualification) : 1.0; // Handle null
        
        this.salary = (baseSalary + experienceIncrement) * qualificationMultiplier;
    }

    private double getQualificationMultiplier(ProfessionalQualification qualification) {
        if (qualification == null) return 1.0; // Default multiplier for undefined qualification

        switch (qualification) {
            case PRIMARY_SCHOOL_FOURTH_GRADE: return 1.0;
            case BASIC_SCHOOL: return 1.1;
            case VOCATIONAL_HIGH_SCHOOL: return 1.2;
            case HIGH_SCHOOL: return 1.3;
            case HIGHER_EDUCATION_SHORT_PROGRAM: return 1.4;
            case COLLEGE: return 1.6;
            case BACHELOR:
            case BACHELOR_PROFESSIONAL: return 1.8;
            case SPECIALIST_PROFESSIONAL:
            case BACHELOR_ACADEMIC: return 2.0;
            case MASTER_INTEGRATED:
            case MASTER: return 2.2;
            case MASTER_OF_SCIENCE: return 2.4;
            case SPECIALIZATION_MEDICINE: return 2.6;
            case SPECIALIST_ACADEMIC: return 2.8;
            case DOCTORATE: return 3.0;
            default: return 1.0;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Staff{name='%s', lastName='%s', gender=%s, birthDate=%s, phoneNumber='%s', username='%s', password='%s', workingExperience=%d, salary=%.2f, professionalQualification='%s'}",
                             getName(), getLastName(), getGender(), getBirthDate(), getPhoneNumber(), getUsername(), getPassword(), getWorkingExperience(), getSalary(), getProfessionalQualification().getDescription());
    }
}
