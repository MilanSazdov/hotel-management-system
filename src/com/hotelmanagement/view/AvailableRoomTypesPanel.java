package com.hotelmanagement.view;

import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AvailableRoomTypesPanel extends JPanel {

    private JTable roomTypeTable;
    private DefaultTableModel model;

    public AvailableRoomTypesPanel() {
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        searchPanel.add(startDateLabel);

        JTextField startDateField = new JTextField(10);
        searchPanel.add(startDateField);

        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        searchPanel.add(endDateLabel);

        JTextField endDateField = new JTextField(10);
        searchPanel.add(endDateField);

        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        roomTypeTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(roomTypeTable);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startDateStr = startDateField.getText();
                String endDateStr = endDateField.getText();
                try {
                    LocalDate startDate = LocalDate.parse(startDateStr);
                    LocalDate endDate = LocalDate.parse(endDateStr);

                    if (endDate.isBefore(startDate)) {
                        JOptionPane.showMessageDialog(AvailableRoomTypesPanel.this, "End date cannot be before start date.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        updateRoomTypeTable(startDate, endDate);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AvailableRoomTypesPanel.this, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void updateRoomTypeTable(LocalDate startDate, LocalDate endDate) {
        String[] columnNames = {"Room Type", "Number of Available Rooms"};
        model = new DefaultTableModel(columnNames, 0);

        RoomController roomController = RoomController.getInstance();
        List<Room> rooms = roomController.getRoomList();

        Set<RoomType> availableRoomTypes = new HashSet<>();
        for (Room room : rooms) {
            boolean isAvailable = true;
            List<LocalDate> checkInDates = room.getCheckInDates();
            List<LocalDate> checkOutDates = room.getCheckOutDates();

            for (int i = 0; i < checkInDates.size(); i++) {
                LocalDate checkIn = checkInDates.get(i);
                LocalDate checkOut = checkOutDates.get(i);

                if (!(endDate.isBefore(checkIn) || startDate.isAfter(checkOut))) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {
                availableRoomTypes.add(room.getRoomType());
            }
        }

        for (RoomType roomType : availableRoomTypes) {
            long count = rooms.stream().filter(room -> room.getRoomType().equals(roomType) && isRoomAvailable(room, startDate, endDate)).count();
            Object[] row = {
                roomType.getCategory(),
                count
            };
            model.addRow(row);
        }

        roomTypeTable.setModel(model);
    }

    private boolean isRoomAvailable(Room room, LocalDate startDate, LocalDate endDate) {
        List<LocalDate> checkInDates = room.getCheckInDates();
        List<LocalDate> checkOutDates = room.getCheckOutDates();

        for (int i = 0; i < checkInDates.size(); i++) {
            LocalDate checkIn = checkInDates.get(i);
            LocalDate checkOut = checkOutDates.get(i);

            if (!(endDate.isBefore(checkIn) || startDate.isAfter(checkOut))) {
                return false;
            }
        }
        return true;
    }
}
