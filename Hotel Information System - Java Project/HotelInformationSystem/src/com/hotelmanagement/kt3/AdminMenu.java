package com.hotelmanagement.kt3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.controller.AdminController;
import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.controller.PriceListController;
import com.hotelmanagement.controller.ReceptionistController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.model.AdditionalServices;
import com.hotelmanagement.model.Admin;
import com.hotelmanagement.model.Gender;
import com.hotelmanagement.model.Maid;
import com.hotelmanagement.model.PriceList;
import com.hotelmanagement.model.ProfessionalQualification;
import com.hotelmanagement.model.Receptionist;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;
import com.hotelmanagement.model.RoomType;
import com.hotelmanagement.model.Staff;

public class AdminMenu {

    public static void show() {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add Receptionist");
            System.out.println("2. Add Maid");
            System.out.println("3. View all employee details");
            System.out.println("4. View revenue and expenses for a specific period");
            System.out.println("5. Create Price List");
            System.out.println("6. Logout");

            System.out.print("Enter your choice: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    addReceptionist(scanner);
                    break;
                case 2:
                    addMaid(scanner);
                    break;
                case 3:
                    viewAllEmployees();
                    break;
                case 4:
                	viewRevenueAndExpenses();
                	break;
                case 5:
                	createPriceList();
                	break;
                case 6:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 6);

        scanner.close();
    }
    
    private static void viewRevenueAndExpenses() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the start date for the revenue and expenses report (YYYY-MM-DD):");
        LocalDate startDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println("Enter the end date for the revenue and expenses report (YYYY-MM-DD):");
        LocalDate endDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);

        if (startDate.isAfter(endDate)) {
            System.out.println("Start date must be before end date.");
            return;
        }

        // Calculate total revenues
        double totalRevenue = 0.0;
        ArrayList<Reservation> reservations = ReservationController.getInstance().getReservationsInDateRange(startDate, endDate);
        for (Reservation res : reservations) {
            if (res.getStatus() == ReservationStatus.CONFIRMED || res.getStatus() == ReservationStatus.CHECKED_IN) {
                totalRevenue += res.getTotalCost();
            }
        }

        // Calculate total expenses assuming you have a method to get all staff members
        double totalExpenses = 0.0;
        ArrayList<Maid> maids = MaidController.getInstance().getAllMaids();
        ArrayList<Receptionist> receptionists = ReceptionistController.getInstance().getAllReceptionists();
        long daysInRange = ChronoUnit.DAYS.between(startDate, endDate) + 1; // Including both start and end date

        ArrayList<Staff> allStaff = new ArrayList<>();
        allStaff.addAll(maids);
        allStaff.addAll(receptionists);

        for (Staff member : allStaff) {
            double dailySalary = member.getSalary() / 30; // Approximate daily salary assuming 30 days per month
            totalExpenses += dailySalary * daysInRange;
        }

        System.out.println("Total Revenue from " + startDate + " to " + endDate + ": $" + String.format("%.2f", totalRevenue));
        System.out.println("Total Expenses from " + startDate + " to " + endDate + ": $" + String.format("%.2f", totalExpenses));
        System.out.println("Net Revenue: $" + String.format("%.2f", totalRevenue - totalExpenses));
    }



    private static void addReceptionist(Scanner scanner) {
        System.out.println("Enter details for new receptionist:");

        System.out.print("Enter first name: ");
        String name = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter gender (MALE, FEMALE, OTHER): ");
        String genderInput = scanner.nextLine();
        Gender gender = Gender.valueOf(genderInput.toUpperCase());

        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();
        LocalDate birthDate = LocalDate.parse(dob, DateTimeFormatter.ISO_LOCAL_DATE);

        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter working experience (years): ");
        int workingExperience = scanner.nextInt();
        scanner.nextLine();  // Consume the newline after number input

        System.out.print("Enter salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine();  // Consume the newline after number input

        System.out.print("Enter professional qualification (e.g., COLLEGE, BACHELOR): ");
        String qualification = scanner.nextLine();
        ProfessionalQualification professionalQualification = ProfessionalQualification.valueOf(qualification.toUpperCase());

        int id = ReceptionistController.getInstance().getNextId();  // Ensure this method is implemented properly

        Receptionist receptionist = new Receptionist(id, name, lastName, gender, birthDate, phoneNumber, username, password, workingExperience, salary, professionalQualification);
        ReceptionistController.getInstance().addReceptionist(receptionist);

        // Immediately save the new receptionist to file
        ReceptionistController.getInstance().saveReceptionistsToFile();

        System.out.println("Receptionist added successfully.");
    }

    private static void addMaid(Scanner scanner) {
        System.out.println("Enter details for new maid:");

        System.out.print("Enter first name: ");
        String name = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter gender (MALE, FEMALE, OTHER): ");
        String genderInput = scanner.nextLine();
        Gender gender = Gender.valueOf(genderInput.toUpperCase());

        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();
        LocalDate birthDate = LocalDate.parse(dob, DateTimeFormatter.ISO_LOCAL_DATE);

        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter working experience (years): ");
        int workingExperience = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter professional qualification (e.g., COLLEGE, BACHELOR): ");
        String qualification = scanner.nextLine();
        ProfessionalQualification professionalQualification = ProfessionalQualification.valueOf(qualification.toUpperCase());

        int id = MaidController.getInstance().getNextId();
        ArrayList<Integer> initialRoomIds = new ArrayList<>();

        Maid maid = new Maid(id, name, lastName, gender, birthDate, phoneNumber, username, password, workingExperience, salary, professionalQualification, initialRoomIds);
        MaidController.getInstance().addMaid(maid);
        
        MaidController.getInstance().saveMaidsToFile();
        
        System.out.println("Maid added successfully. Username: " + username + ", Password: " + password);
    }

    private static void viewAllEmployees() {
        System.out.println("Fetching all employee details...");

        // Get details of all receptionists
        ArrayList<Receptionist> receptionists = ReceptionistController.getInstance().getAllReceptionists();
        if (receptionists.isEmpty()) {
            System.out.println("No receptionists found.");
        } else {
            System.out.println("\nReceptionists:");
            for (Receptionist receptionist : receptionists) {
                System.out.println(receptionist);
            }
        }

        // Get details of all maids
        ArrayList<Maid> maids = MaidController.getInstance().getAllMaids();
        if (maids.isEmpty()) {
            System.out.println("No maids found.");
        } else {
            System.out.println("\nMaids:");
            for (Maid maid : maids) {
                System.out.println(maid);
            }
        }

        ArrayList<Admin> admins = AdminController.getInstance().getAllAdmins();
        if (admins.isEmpty()) {
            System.out.println("No administrators found.");
        } else {
            System.out.println("\nAdministrators:");
            for (Admin admin : admins) {
                System.out.println(admin);
            }
        }
    }
    
    private static void createPriceList() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Creating a new price list:");

        // Get the valid from and to dates
        System.out.print("Enter the start date for the price list (YYYY-MM-DD): ");
        LocalDate validFrom = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.print("Enter the end date for the price list (YYYY-MM-DD): ");
        LocalDate validTo = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);

        // Check if the new date range overlaps with existing price lists
        if (PriceListController.getInstance().isDateRangeOverlapping(validFrom, validTo)) {
            System.out.println("Error: The specified date range overlaps with an existing price list.");
            return;
        }

        PriceList newPriceList = new PriceList(validFrom, validTo);

        // Adding prices for room types
        System.out.println("Enter prices for room types:");
        ArrayList<RoomType> roomTypes = RoomTypeController.getInstance().getRoomTypeList();
        for (RoomType roomType : roomTypes) {
            System.out.print("Enter price for " + roomType.getCategory() + ": ");
            double price = scanner.nextDouble();
            newPriceList.setPriceForRoomTypeId(roomType.getRoomTypeId(), price);
        }
        scanner.nextLine(); // Consume newline left-over

        // Adding prices for additional services
        System.out.println("Enter prices for additional services:");
        ArrayList<AdditionalServices> additionalServices = AdditionalServicesController.getInstance().getAdditionalServicesList();
        for (AdditionalServices service : additionalServices) {
            System.out.print("Enter price for " + service.getServiceName() + ": ");
            double price = scanner.nextDouble();
            newPriceList.setPriceForAdditionalServiceId(service.getServiceId(), price);
        }
        scanner.nextLine(); // Consume newline left-over

        // Add this new price list to the controller and save to file
        PriceListController.getInstance().addPriceList(newPriceList);
        PriceListController.getInstance().savePriceListsToFile("src/com/hotelmanagement/data/price_list.csv");

        System.out.println("New price list created successfully with ID: " + newPriceList.getPriceListId());

    }




}
