package com.hotelmanagement.view;

import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.AdditionalServices;
import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.model.RoomType;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.controller.GuestController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationsPanelReceptionist extends JPanel {

    private JTable reservationsTable;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JLabel noReservationsLabel;
    private JComboBox<String> roomTypeComboBox;
    private JTextField roomIdField;
    private JTextField minPriceField;
    private JTextField maxPriceField;
    private JComboBox<String> additionalServicesComboBox;

    public ReservationsPanelReceptionist() {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("All Reservations", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        add(label, BorderLayout.NORTH);

        reservationsTable = new JTable();
        reservationsTable.setFillsViewportHeight(true);
        reservationsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        reservationsTable.setRowHeight(30);
        scrollPane = new JScrollPane(reservationsTable);
        add(scrollPane, BorderLayout.CENTER);

        noReservationsLabel = new JLabel("There are currently no reservations.", SwingConstants.CENTER);
        noReservationsLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(new JLabel("Room Type:"), gbc);
        roomTypeComboBox = new JComboBox<>();
        roomTypeComboBox.addItem("All Room Types");
        List<RoomType> roomTypes = RoomTypeController.getInstance().getRoomTypeList();
        for (RoomType roomType : roomTypes) {
            roomTypeComboBox.addItem(roomType.getCategory().name());
        }
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        filterPanel.add(roomTypeComboBox, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        filterPanel.add(new JLabel("Room ID:"), gbc);
        roomIdField = new JTextField(10);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        filterPanel.add(roomIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        filterPanel.add(new JLabel("Min Price:"), gbc);
        minPriceField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        filterPanel.add(minPriceField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        filterPanel.add(new JLabel("Max Price:"), gbc);
        maxPriceField = new JTextField(10);
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        filterPanel.add(maxPriceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        filterPanel.add(new JLabel("Additional Service:"), gbc);
        additionalServicesComboBox = new JComboBox<>();
        additionalServicesComboBox.addItem("All Services");
        List<AdditionalServices> services = AdditionalServicesController.getInstance().getAdditionalServicesList();
        for (AdditionalServices service : services) {
            additionalServicesComboBox.addItem(service.getServiceName());
        }
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        filterPanel.add(additionalServicesComboBox, gbc);

        add(filterPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> updateReservationsTable());

        updateReservationsTable();
    }

    public void updateReservationsTable() {
        String[] columnNames = {"Reservation ID", "Guest Nickname", "Room ID", "Room Type", "Check-In Date", "Check-Out Date", "Status", "Additional Services", "Total Cost"};
        model = new DefaultTableModel(columnNames, 0);

        ReservationController reservationController = ReservationController.getInstance();
        List<Reservation> reservations = reservationController.getAllReservations();

        if (reservations.isEmpty()) {
            reservationsTable.setModel(new DefaultTableModel());
            remove(scrollPane);
            add(noReservationsLabel, BorderLayout.CENTER);
        } else {
            remove(noReservationsLabel);

            String selectedRoomType = (String) roomTypeComboBox.getSelectedItem();
            String roomIdText = roomIdField.getText();
            String minPriceText = minPriceField.getText();
            String maxPriceText = maxPriceField.getText();
            String selectedService = (String) additionalServicesComboBox.getSelectedItem();

            reservations = reservations.stream()
                .filter(reservation -> {
                    // Filter by room type
                    if (!selectedRoomType.equals("All Room Types")) {
                        RoomType roomType = RoomTypeController.getInstance().searchRoomTypeById(reservation.getRoomTypeId());
                        if (roomType == null || !roomType.getCategory().name().equals(selectedRoomType)) {
                            return false;
                        }
                    }
                    // Filter by room ID
                    if (!roomIdText.isEmpty()) {
                        try {
                            int roomId = Integer.parseInt(roomIdText);
                            if (reservation.getRoomId() != roomId) {
                                return false;
                            }
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                    // Filter by price range
                    if (!minPriceText.isEmpty()) {
                        try {
                            double minPrice = Double.parseDouble(minPriceText);
                            if (reservation.getTotalCost() < minPrice) {
                                return false;
                            }
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                    if (!maxPriceText.isEmpty()) {
                        try {
                            double maxPrice = Double.parseDouble(maxPriceText);
                            if (reservation.getTotalCost() > maxPrice) {
                                return false;
                            }
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                    // Filter by additional services
                    if (!selectedService.equals("All Services")) {
                        List<Integer> serviceIds = reservation.getAdditionalServiceIds();
                        List<String> serviceNames = serviceIds.stream()
                            .map(serviceId -> AdditionalServicesController.getInstance().getAdditionalServiceById(serviceId))
                            .map(AdditionalServices::getServiceName)
                            .collect(Collectors.toList());
                        if (!serviceNames.contains(selectedService)) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());

            for (Reservation reservation : reservations) {
                RoomType roomType = RoomTypeController.getInstance().searchRoomTypeById(reservation.getRoomTypeId());
                String roomTypeName = roomType != null ? roomType.getCategory().name() : "Unknown";

                String guestNickname = GuestController.getInstance().getGuestById(reservation.getGuestId()).getUsername();

                List<String> serviceNames = reservation.getAdditionalServiceIds().stream()
                        .map(serviceId -> AdditionalServicesController.getInstance().getAdditionalServiceById(serviceId))
                        .map(AdditionalServices::getServiceName)
                        .collect(Collectors.toList());
                String additionalServices = String.join(" ", serviceNames);

                Object[] row = {
                    reservation.getReservationId(),
                    guestNickname,
                    reservation.getRoomId(),
                    roomTypeName,
                    reservation.getCheckInDate(),
                    reservation.getCheckOutDate(),
                    reservation.getStatus(),
                    additionalServices,
                    reservation.getTotalCost()
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

    private void configureAdditionalServicesColumn() {
        TableColumn additionalServicesColumn = reservationsTable.getColumnModel().getColumn(7);
        additionalServicesColumn.setCellEditor(new JTextAreaCellEditor());
    }
}
