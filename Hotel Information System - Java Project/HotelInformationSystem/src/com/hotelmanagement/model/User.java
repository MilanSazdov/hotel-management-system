package com.hotelmanagement.model;

import java.time.LocalDate;
import java.util.Date;

public abstract class User {
	
	protected String name;
	protected String lastName;
	protected Gender gender;
	protected LocalDate birthDate;
	protected String phoneNumber;
	protected String username;
	protected String password;
	
	public User(String name, String lastName, Gender gender, LocalDate currentDate, String phoneNumber, String username, String password) {
        this.name = name;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = currentDate;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
	}
	
	public User() {
		this.name = null;
	    this.lastName = null;
	    this.gender = Gender.NOT_SPECIFIED;
	    this.birthDate = null;
	    this.phoneNumber = null;
	    this.username = null;
	    this.password = null;
	}
	
	public String getName() {
		return this.name;
    }
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Gender getGender() {
        return this.gender;
	}
	
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public LocalDate getBirthDate() {
		return this.birthDate;
	}
	
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public void setPhoneNumber(String number) {
		this.phoneNumber = number;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
    }
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "User{" +
		           "name='" + name + '\'' +
		           ", lastName='" + lastName + '\'' +
		           ", gender=" + gender +
		           ", birthDate=" + birthDate +
		           ", phoneNumber=" + phoneNumber +
		           ", username='" + username + '\'' +
		           ", password='" + password + '\'' +
		           '}';
		}
}
