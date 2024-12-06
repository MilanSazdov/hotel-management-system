package com.hotelmanagement.view;

import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.model.Maid;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomType;

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
import java.util.HashMap;
import java.util.Map;

public class ReportForMaidsPanel extends JPanel {

    private JTextField startDateField;
    private JTextField endDateField;
    private JTable cleanedRoomsTable;
    private DefaultTableModel model;
    private JScrollPane scrollPane;

    public ReportForMaidsPanel() {
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

        cleanedRoomsTable = new JTable();
        cleanedRoomsTable.setFillsViewportHeight(true);
        cleanedRoomsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        cleanedRoomsTable.setRowHeight(30);
        scrollPane = new JScrollPane(cleanedRoomsTable);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startDate = startDateField.getText();
                String endDate = endDateField.getText();
                if (validateDates(startDate, endDate)) {
                    updateCleanedRoomsTable(startDate, endDate);
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

    private void updateCleanedRoomsTable(String startDate, String endDate) {
        String[] columnNames = {"Maid ID", "Room ID", "Room Number", "Room Type", "Cleaning Date"};
        model = new DefaultTableModel(columnNames, 0);

        try {
            Map<Integer, Maid> maids = new HashMap<>();
            for (Maid maid : MaidController.getInstance().getAllMaids()) {
                maids.put(maid.getMaidId(), maid);
            }

            boolean roomsFound = false;
            try (BufferedReader br = new BufferedReader(new FileReader("src/com/hotelmanagement/data/cleaned_rooms_by_period.csv"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    int maidId = Integer.parseInt(values[0]);
                    int roomId = Integer.parseInt(values[1]);
                    LocalDate cleaningDate = LocalDate.parse(values[2]);

                    LocalDate start = LocalDate.parse(startDate);
                    LocalDate end = LocalDate.parse(endDate);

                    if ((cleaningDate.isEqual(start) || cleaningDate.isAfter(start)) &&
                        (cleaningDate.isEqual(end) || cleaningDate.isBefore(end))) {
                        Room room = RoomController.getInstance().findRoomById(roomId);
                        if (room != null) {
                            Object[] row = {
                                maidId,
                                roomId,
                                room.getRoomNumber(),
                                room.getRoomType().getCategory().name(),
                                cleaningDate
                            };
                            model.addRow(row);
                            roomsFound = true;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (roomsFound) {
                cleanedRoomsTable.setModel(model);
                add(scrollPane, BorderLayout.CENTER);
            } else {
                JOptionPane.showMessageDialog(this, "No cleaned rooms found for the selected date range.", "No Results", JOptionPane.INFORMATION_MESSAGE);
                cleanedRoomsTable.setModel(new DefaultTableModel());
                remove(scrollPane);
            }

            revalidate();
            repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading cleaned rooms: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
