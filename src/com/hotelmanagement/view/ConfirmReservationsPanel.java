package com.hotelmanagement.view;

import com.hotelmanagement.controller.*;
import com.hotelmanagement.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConfirmReservationsPanel extends JPanel {

    private JTable reservationsTable;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JLabel noReservationsLabel;
    private int receptionistId;

    private JComboBox<String> roomTypeFilter;
    private JTextField minPriceFilter;
    private JTextField maxPriceFilter;
    private JComboBox<String> additionalServiceFilter;

    public ConfirmReservationsPanel(int receptionistId) {
        this.receptionistId = receptionistId;
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Confirm Reservations", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        add(label, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));

        roomTypeFilter = new JComboBox<>(new String[]{"All Room Types", "SINGLE", "DOUBLE", "TRIPLE", "QUAD", "FIVE_BED"});
        minPriceFilter = new JTextField(5);
        maxPriceFilter = new JTextField(5);
        additionalServiceFilter = new JComboBox<>(new String[]{
                "All Services", "breakfast", "lunch", "dinner", "pool", "spa",
                "laundry", "room cleaning", "airport shuttle", "fitness center access", "room service"
        });

        filterPanel.add(new JLabel("Room Type:"));
        filterPanel.add(roomTypeFilter);
        filterPanel.add(new JLabel("Min Price:"));
        filterPanel.add(minPriceFilter);
        filterPanel.add(new JLabel("Max Price:"));
        filterPanel.add(maxPriceFilter);
        filterPanel.add(new JLabel("Additional Service:"));
        filterPanel.add(additionalServiceFilter);

        add(filterPanel, BorderLayout.NORTH);

        reservationsTable = new JTable();
        reservationsTable.setFillsViewportHeight(true);
        reservationsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        reservationsTable.setRowHeight(30);
        scrollPane = new JScrollPane(reservationsTable);
        add(scrollPane, BorderLayout.CENTER);

        noReservationsLabel = new JLabel("There are currently no reservations to confirm.", SwingConstants.CENTER);
        noReservationsLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton confirmButton = new JButton("Confirm");
        JButton rejectButton = new JButton("Reject");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(confirmButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        updateReservationsTable();

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = reservationsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int reservationId = (int) reservationsTable.getValueAt(selectedRow, 0);
                    confirmReservation(reservationId);
                } else {
                    JOptionPane.showMessageDialog(ConfirmReservationsPanel.this, "Please select a reservation to confirm.");
                }
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = reservationsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int reservationId = (int) reservationsTable.getValueAt(selectedRow, 0);
                    rejectReservation(reservationId);
                } else {
                    JOptionPane.showMessageDialog(ConfirmReservationsPanel.this, "Please select a reservation to reject.");
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateReservationsTable();
            }
        });

        roomTypeFilter.addActionListener(e -> updateReservationsTable());
        minPriceFilter.addActionListener(e -> updateReservationsTable());
        maxPriceFilter.addActionListener(e -> updateReservationsTable());
        additionalServiceFilter.addActionListener(e -> updateReservationsTable());
    }

    public void updateReservationsTable() {
        String[] columnNames = {"Reservation ID", "Guest Nickname", "Room Type", "Check-In Date", "Check-Out Date", "Additional Services", "Total Cost", "Status"};
        model = new DefaultTableModel(columnNames, 0);

        ReservationController reservationController = ReservationController.getInstance();
        ArrayList<Reservation> reservations = reservationController.getReservationsByStatus(ReservationStatus.WAITING);

        String selectedRoomType = roomTypeFilter.getSelectedItem().toString();
        String minPriceText = minPriceFilter.getText();
        String maxPriceText = maxPriceFilter.getText();
        String selectedService = additionalServiceFilter.getSelectedItem().toString();

        if (reservations.isEmpty()) {
            reservationsTable.setModel(new DefaultTableModel());
            remove(scrollPane);
            add(noReservationsLabel, BorderLayout.CENTER);
        } else {
            remove(noReservationsLabel);

            reservations = (ArrayList<Reservation>) reservations.stream()
                .filter(reservation -> (selectedRoomType.equals("All Room Types") || RoomTypeController.getInstance().searchRoomTypeById(reservation.getRoomTypeId()).getCategory().name().equals(selectedRoomType)))
                .filter(reservation -> (minPriceText.isEmpty() || reservation.getTotalCost() >= Double.parseDouble(minPriceText)))
                .filter(reservation -> (maxPriceText.isEmpty() || reservation.getTotalCost() <= Double.parseDouble(maxPriceText)))
                .filter(reservation -> (selectedService.equals("All Services") || reservation.getAdditionalServiceIds().stream()
                    .map(serviceId -> AdditionalServicesController.getInstance().getAdditionalServiceById(serviceId))
                    .map(AdditionalServices::getServiceName)
                    .collect(Collectors.toList()).contains(selectedService)))
                .collect(Collectors.toList());

            for (Reservation reservation : reservations) {
                String guestNickname = GuestController.getInstance().getGuestById(reservation.getGuestId()).getUsername();
                RoomType roomType = RoomTypeController.getInstance().searchRoomTypeById(reservation.getRoomTypeId());
                String roomTypeName = roomType != null ? roomType.getCategory().name() : "Unknown";

                ArrayList<String> serviceNames = reservation.getAdditionalServiceIds().stream()
                        .map(serviceId -> AdditionalServicesController.getInstance().getAdditionalServiceById(serviceId))
                        .map(AdditionalServices::getServiceName)
                        .collect(Collectors.toCollection(ArrayList::new));
                String additionalServices = String.join(", ", serviceNames);

                Object[] row = {
                    reservation.getReservationId(),
                    guestNickname,
                    roomTypeName,
                    reservation.getCheckInDate(),
                    reservation.getCheckOutDate(),
                    additionalServices,
                    reservation.getTotalCost(),
                    reservation.getStatus()
                };
                model.addRow(row);
            }
            reservationsTable.setModel(model);
            configureAdditionalServicesColumn();
            add(scrollPane, BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }

    private void confirmReservation(int reservationId) {
        ReservationController reservationController = ReservationController.getInstance();
        RoomController roomController = RoomController.getInstance();

        Reservation reservation = reservationController.findReservationById(reservationId);
        if (reservation != null && reservation.getStatus() == ReservationStatus.WAITING) {
            RoomType roomType = RoomTypeController.getInstance().searchRoomTypeById(reservation.getRoomTypeId());
            if (roomType != null) {
                ArrayList<Room> availableRooms = roomController.getFreeRoomsByTypeAndPeriod(roomType, reservation.getCheckInDate(), reservation.getCheckOutDate());
                if (!availableRooms.isEmpty()) {
                    Room room = availableRooms.get(0);
                    reservation.setRoomId(room.getRoomId());
                    reservation.setStatus(ReservationStatus.CONFIRMED);

                    room.addCheckInDate(reservation.getCheckInDate());
                    room.addCheckOutDate(reservation.getCheckOutDate());

                    reservationController.updateReservation(reservation);
                    roomController.saveRoomsToFile();
                    logReservation("src/com/hotelmanagement/data/confirmed_reservations_by_period.csv", reservation, "confirmed");
                    updateReservationsTable();
                    JOptionPane.showMessageDialog(this, "Reservation confirmed successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "No available rooms of the required type.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Room type for the reservation is not found.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Reservation is not in a confirmable state.");
        }
    }

    private void rejectReservation(int reservationId) {
        ReservationController reservationController = ReservationController.getInstance();
        Reservation reservation = reservationController.findReservationById(reservationId);
        if (reservation != null && reservation.getStatus() == ReservationStatus.WAITING) {
            reservation.setStatus(ReservationStatus.REJECTED);
            reservationController.updateReservation(reservation);
            logReservation("src/com/hotelmanagement/data/rejected_reservations_by_period.csv", reservation, "rejected");
            updateReservationsTable();
            JOptionPane.showMessageDialog(this, "Reservation rejected successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Reservation is not in a rejectable state.");
        }
    }

    private void logReservation(String fileName, Reservation reservation, String action) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.format("%d,%d,%s\n", receptionistId, reservation.getReservationId(), LocalDate.now().toString()));
            writer.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error logging " + action + " reservation: " + e.getMessage());
        }
    }

    private void configureAdditionalServicesColumn() {
        TableColumn additionalServicesColumn = reservationsTable.getColumnModel().getColumn(5);
        additionalServicesColumn.setCellEditor(new JTextAreaCellEditor());
    }
}
