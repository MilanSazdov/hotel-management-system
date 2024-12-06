package com.hotelmanagement.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReportForReservationsPanel extends JPanel {

    private JTextField startDateField;
    private JTextField endDateField;
    private JPanel resultPanel;

    public ReportForReservationsPanel() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel startDateLabel = new JLabel("Enter Start Date (YYYY-MM-DD):");
        startDateField = new JTextField(10);
        JLabel endDateLabel = new JLabel("Enter End Date (YYYY-MM-DD):");
        endDateField = new JTextField(10);
        JButton searchButton = new JButton("Search");

        inputPanel.add(startDateLabel);
        inputPanel.add(startDateField);
        inputPanel.add(endDateLabel);
        inputPanel.add(endDateField);
        inputPanel.add(searchButton);

        add(inputPanel, BorderLayout.NORTH);

        resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(4, 1, 10, 10));
        add(resultPanel, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startDate = startDateField.getText();
                String endDate = endDateField.getText();
                if (validateDates(startDate, endDate)) {
                    updateReservationCounts(startDate, endDate);
                }
            }
        });
    }

    private boolean validateDates(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            if (start.isAfter(end)) {
                JOptionPane.showMessageDialog(this, "Start date cannot be after end date.", "Invalid Dates", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Invalid Dates", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void updateReservationCounts(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        resultPanel.removeAll(); // Clear the panel

        addCategoryPanel("Confirmed Reservations", "src/com/hotelmanagement/data/confirmed_reservations_by_period.csv", start, end);
        addCategoryPanel("Changed Mind Reservations", "src/com/hotelmanagement/data/changed_mind_reservations_by_period.csv", start, end);
        addCategoryPanel("Cancelled Reservations", "src/com/hotelmanagement/data/canceled_reservations_by_period.csv", start, end);
        addCategoryPanel("Rejected Reservations", "src/com/hotelmanagement/data/rejected_reservations_by_period.csv", start, end);

        revalidate();
        repaint();
    }

    private void addCategoryPanel(String category, String filePath, LocalDate startDate, LocalDate endDate) {
        JPanel categoryPanel = new JPanel(new BorderLayout());
        JLabel categoryLabel = new JLabel(category + ": ");
        categoryPanel.add(categoryLabel, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID Type", "ID", "Reservation ID", "Date"}, 0);
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);

        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                LocalDate date = LocalDate.parse(values[2]);

                if ((date.isEqual(startDate) || date.isAfter(startDate)) && (date.isEqual(endDate) || date.isBefore(endDate))) {
                    count++;
                    String idType = filePath.contains("rejected_reservations_by_period.csv") || filePath.contains("confirmed_reservations_by_period.csv") ? "Receptionist ID" : "Guest ID";
                    Object[] row = {idType, values[0], values[1], date.toString()};
                    model.addRow(row);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (count == 0) {
            model.addRow(new Object[]{"No reservations for this period"});
        } else {
            categoryLabel.setText(category + ": " + count);
        }

        categoryPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        resultPanel.add(categoryPanel);
    }
}
