package com.hotelmanagement.view;

import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DailyActivitiesPanel extends JPanel {

    private JTable arrivalsTable;
    private JTable departuresTable;
    private JLabel occupancyLabel;
    private JLabel totalRoomsLabel;
    private JLabel occupiedRoomsLabel;

    public DailyActivitiesPanel() {
        setLayout(new BorderLayout());

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2));

        arrivalsTable = new JTable();
        departuresTable = new JTable();
        JScrollPane arrivalsScrollPane = new JScrollPane(arrivalsTable);
        JScrollPane departuresScrollPane = new JScrollPane(departuresTable);

        tablesPanel.add(arrivalsScrollPane);
        tablesPanel.add(departuresScrollPane);

        add(tablesPanel, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new GridLayout(3, 1));
        occupancyLabel = new JLabel();
        totalRoomsLabel = new JLabel();
        occupiedRoomsLabel = new JLabel();

        summaryPanel.add(occupancyLabel);
        summaryPanel.add(totalRoomsLabel);
        summaryPanel.add(occupiedRoomsLabel);

        add(summaryPanel, BorderLayout.SOUTH);

        updateTables();
        updateOccupancyInfo();
    }

    private void updateTables() {
        LocalDate today = LocalDate.now();
        ReservationController reservationController = ReservationController.getInstance();

        List<Reservation> arrivals = reservationController.getAllReservations()
                .stream()
                .filter(reservation -> reservation.getCheckInDate().isEqual(today) && reservation.getStatus() == ReservationStatus.CHECKED_IN)
                .collect(Collectors.toList());

        List<Reservation> departures = reservationController.getAllReservations()
                .stream()
                .filter(reservation -> reservation.getCheckOutDate().isEqual(today) && reservation.getStatus() == ReservationStatus.CHECKED_OUT)
                .collect(Collectors.toList());

        String[] columnNames = {"Reservation ID", "Guest ID", "Room ID", "Room Type", "Check-In Date", "Check-Out Date", "Status"};
        DefaultTableModel arrivalsModel = new DefaultTableModel(columnNames, 0);
        DefaultTableModel departuresModel = new DefaultTableModel(columnNames, 0);

        for (Reservation reservation : arrivals) {
            arrivalsModel.addRow(new Object[]{
                reservation.getReservationId(),
                reservation.getGuestId(),
                reservation.getRoomId(),
                RoomTypeController.getInstance().searchRoomTypeById(reservation.getRoomTypeId()).getCategory().name(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getStatus()
            });
        }

        for (Reservation reservation : departures) {
            departuresModel.addRow(new Object[]{
                reservation.getReservationId(),
                reservation.getGuestId(),
                reservation.getRoomId(),
                RoomTypeController.getInstance().searchRoomTypeById(reservation.getRoomTypeId()).getCategory().name(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getStatus()
            });
        }

        arrivalsTable.setModel(arrivalsModel);
        departuresTable.setModel(departuresModel);
    }

    private void updateOccupancyInfo() {
        RoomController roomController = RoomController.getInstance();
        List<Room> allRooms = roomController.getAllRooms();
        long totalRooms = allRooms.size();
        long occupiedRooms = allRooms.stream().filter(room -> room.getStatus() == RoomStatus.OCCUPIED).count();

        occupancyLabel.setText("Hotel Occupancy: " + (occupiedRooms * 100 / totalRooms) + "%");
        totalRoomsLabel.setText("Total Rooms: " + totalRooms);
        occupiedRoomsLabel.setText("Occupied Rooms: " + occupiedRooms);
    }
}
