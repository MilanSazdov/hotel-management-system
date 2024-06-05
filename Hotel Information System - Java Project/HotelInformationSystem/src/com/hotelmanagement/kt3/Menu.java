package com.hotelmanagement.kt3;

import com.hotelmanagement.controller.DataController;
import com.hotelmanagement.model.User;

public class Menu {
    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int option = readOption();
            switch (option) {
            case 1:
                // Login
            	loginUser();
                break;
            case 2:
                System.out.println("Goodbye!");
                return; // Exit the program
            default:
                System.out.println("Invalid option. Please try again.");
                continue;
            }
        }
    }
    
    private static void loginUser() {
        System.out.println("Enter username: ");
        String username = readString();
        System.out.println("Enter password: ");
        String password = readString();

        // Access the singleton instance of DataController directly
        String role = DataController.getInstance().authenticateUser(username, password);
        if (role != null) {
            System.out.println("Login successful. Welcome, " + role + "!");
            switch (role) {
                case "Admin":
                    // AdminMenu.show();
                    break;
                case "Maid":
                    // MaidMenu.show();
                    break;
                case "Receptionist":
                    ReceptionistMenu.show();
                    break;
                case "Guest":
                    GuestMenu.show();
                    break;
                default:
                    System.out.println("Access denied. Unknown role.");
                    break;
            }
        } else {
            System.out.println("User not found or invalid credentials. Please try again.");
        }
    }


    private static void displayMenu() {
        System.out.println("Welcome to the Hotel !!!");
        System.out.println("Please select an option:");
        System.out.println("1. Login");
        System.out.println("2. Exit");
    }

    private static int readOption() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Enter option: ");
        int option = scanner.nextInt();
        return option;
    }
    

	private static String readString() {
	    java.util.Scanner scanner = new java.util.Scanner(System.in);
	    return scanner.nextLine();
	}

}

