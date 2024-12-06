package com.hotelmanagement.view;

import com.hotelmanagement.controller.AdminController;
import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.controller.ReceptionistController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.model.Admin;
import com.hotelmanagement.model.Maid;
import com.hotelmanagement.model.Receptionist;
import com.hotelmanagement.model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class ReportsPanel extends JPanel {

    private JTextField startDateField;
    private JTextField endDateField;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JTextArea incomeDetails;
    private JTextArea expenseDetails;

    public ReportsPanel() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());

        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        startDateField = new JTextField(10);
        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        endDateField = new JTextField(10);

        JButton calculateButton = new JButton("Calculate");

        inputPanel.add(startDateLabel);
        inputPanel.add(startDateField);
        inputPanel.add(endDateLabel);
        inputPanel.add(endDateField);
        inputPanel.add(calculateButton);

        add(inputPanel, BorderLayout.NORTH);

        JPanel resultPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        incomeLabel = new JLabel("Income: ");
        expenseLabel = new JLabel("Expenses: ");
        incomeDetails = new JTextArea(5, 20);
        incomeDetails.setEditable(false);
        expenseDetails = new JTextArea(5, 20);
        expenseDetails.setEditable(false);

        resultPanel.add(incomeLabel);
        resultPanel.add(new JScrollPane(incomeDetails));
        resultPanel.add(expenseLabel);
        resultPanel.add(new JScrollPane(expenseDetails));

        add(resultPanel, BorderLayout.CENTER);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateIncomeAndExpenses();
            }
        });
    }

    private void calculateIncomeAndExpenses() {
        try {
            LocalDate startDate = LocalDate.parse(startDateField.getText());
            LocalDate endDate = LocalDate.parse(endDateField.getText());
            if (startDate.isAfter(endDate)) {
                JOptionPane.showMessageDialog(this, "Start date must be before end date.");
                return;
            }

            long daysInRange = ChronoUnit.DAYS.between(startDate, endDate) + 1;

            double totalIncome = 0;
            double totalExpenses = 0;
            StringBuilder incomeFactors = new StringBuilder("Income Factors:\n");
            StringBuilder expenseFactors = new StringBuilder("Expense Factors:\n");

            ArrayList<ReservationRecord> confirmedReservations = readReservationsFromFile("src/com/hotelmanagement/data/confirmed_reservations_by_period.csv");

            for (ReservationRecord record : confirmedReservations) {
                if (record.date.isAfter(startDate.minusDays(1)) && record.date.isBefore(endDate.plusDays(1))) {
                    Reservation reservation = ReservationController.getInstance().findReservationById(record.reservationId);
                    if (reservation != null) {
                        totalIncome += reservation.getTotalCost();
                        incomeFactors.append(String.format("Reservation ID: %d, Total Cost: %.2f\n", reservation.getReservationId(), reservation.getTotalCost()));
                    }
                }
            }

            ArrayList<Admin> admins = AdminController.getInstance().getAllAdmins();
            ArrayList<Maid> maids = MaidController.getInstance().getAllMaids();
            ArrayList<Receptionist> receptionists = ReceptionistController.getInstance().getAllReceptionists();

            for (Admin admin : admins) {
                double monthlySalary = admin.getSalary();
                double dailySalary = monthlySalary / 30;
                totalExpenses += dailySalary * daysInRange;
                expenseFactors.append(String.format("Admin ID: %d, Daily Salary: %.2f\n", admin.getAdminId(), dailySalary));
            }

            for (Maid maid : maids) {
                double dailySalary = maid.getSalary() / 30;
                totalExpenses += dailySalary * daysInRange;
                expenseFactors.append(String.format("Maid ID: %d, Daily Salary: %.2f\n", maid.getMaidId(), dailySalary));
            }

            for (Receptionist receptionist : receptionists) {
                double dailySalary = receptionist.getSalary() / 30;
                totalExpenses += dailySalary * daysInRange;
                expenseFactors.append(String.format("Receptionist ID: %d, Daily Salary: %.2f\n", receptionist.getReceptionistId(), dailySalary));
            }

            incomeLabel.setText(String.format("Income: %.2f", totalIncome));
            expenseLabel.setText(String.format("Expenses: %.2f", totalExpenses));
            incomeDetails.setText(incomeFactors.toString());
            expenseDetails.setText(expenseFactors.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error parsing dates or calculating income/expenses: " + ex.getMessage());
        }
    }

    private ArrayList<ReservationRecord> readReservationsFromFile(String filePath) {
        ArrayList<ReservationRecord> reservations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 3) {
                    System.err.println("Invalid line: " + line);
                    continue;
                }
                try {
                    int reservationId = Integer.parseInt(values[1]);
                    LocalDate date = LocalDate.parse(values[2]);
                    reservations.add(new ReservationRecord(reservationId, date));
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return reservations;
    }

    private static class ReservationRecord {
        int reservationId;
        LocalDate date;

        public ReservationRecord(int reservationId, LocalDate date) {
            this.reservationId = reservationId;
            this.date = date;
        }
    }
}
