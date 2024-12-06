package com.hotelmanagement.model;

public enum ProfessionalQualification {
	
	PRIMARY_SCHOOL_FOURTH_GRADE("I Step - Four grades of primary education"),
    BASIC_SCHOOL("II Step - Basic school"),
    VOCATIONAL_HIGH_SCHOOL("III Step - Vocational high school"),
    HIGH_SCHOOL("IV Step - High school"),
    HIGHER_EDUCATION_SHORT_PROGRAM("V Step - Higher education short program"),
    COLLEGE("VI Step - College education"),
    BACHELOR("VII-1 Bachelor's degree"),
    BACHELOR_PROFESSIONAL("VII-1 Professional bachelor's studies"),
    SPECIALIST_PROFESSIONAL("VII-2 Specialist professional studies"),
    BACHELOR_ACADEMIC("VII-1a Academic bachelor's studies"),
    MASTER_INTEGRATED("VII-1a Integrated master studies"),
    MASTER("VII-1b Master's degree"),
    MASTER_OF_SCIENCE("VII-2 Master of Science"),
    SPECIALIZATION_MEDICINE("VII-2 Specialization in medicine"),
    SPECIALIST_ACADEMIC("VII-2 Specialist academic studies"),
    DOCTORATE("VIII Doctorate");
	
	private final String description;
	
	ProfessionalQualification(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}	
}
