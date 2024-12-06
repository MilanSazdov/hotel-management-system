package com.hotelmanagement.view;

import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomType;
import com.hotelmanagement.model.AdditionalServices;

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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ReservationsPanel extends JPanel {

    private JTable reservationsTable;
    private DefaultTableModel model;
    private int guestId;
    private JScrollPane scrollPane;
    private JLabel noReservationsLabel;
    private JLabel totalCostLabel;

    public ReservationsPanel(int guestId) {
        this.guestId = guestId;
        setLayout(new BorderLayout());

        JLabel label = new JLabel("My Reservations", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        add(label, BorderLayout.NORTH);

        reservationsTable = new JTable();
        reservationsTable.setFillsViewportHeight(true);
        reservationsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        reservationsTable.setRowHeight(30);
        scrollPane = new JScrollPane(reservationsTable);
        add(scrollPane, BorderLayout.CENTER);

        noReservationsLabel = new JLabel("You currently have no reservations.", SwingConstants.CENTER);
        noReservationsLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        totalCostLabel = new JLabel("Total Cost: 0.00", SwingConstants.CENTER);
        totalCostLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(totalCostLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton cancelButton = new JButton("Cancel Reservation");
        JButton editButton = new JButton("Edit Reservation");

        buttonPanel.add(cancelButton);
        buttonPanel.add(editButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(totalCostLabel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        updateReservationsTable();

        cancelButton.addActionListener(e -> cancelReservation());

        editButton.addActionListener(e -> editReservation());
    }

    public void updateReservationsTable() {
        String[] columnNames = {"Reservation ID", "Room ID", "Room Type", "Check-In Date", "Check-Out Date", "Status", "Additional Services", "Total Cost", "Amount Spent"};
        model = new DefaultTableModel(columnNames, 0);

        ReservationController reservationController = ReservationController.getInstance();
        ArrayList<Reservation> reservations = reservationController.getReservationsByGuestId(guestId);

        double totalCost = 0;

        if (reservations.isEmpty()) {
            reservationsTable.setModel(new DefaultTableModel());
            remove(scrollPane);
            add(noReservationsLabel, BorderLayout.CENTER);
        } else {
            remove(noReservationsLabel);
            for (Reservation reservation : reservations) {
                RoomType roomType = RoomTypeController.getInstance().searchRoomTypeById(reservation.getRoomTypeId());
                String roomTypeName = roomType != null ? roomType.getCategory().name() : "Unknown";

                ArrayList<String> serviceNames = reservation.getAdditionalServiceIds().stream()
                        .map(serviceId -> AdditionalServicesController.getInstance().getAdditionalServiceById(serviceId))
                        .map(AdditionalServices::getServiceName)
                        .collect(Collectors.toCollection(ArrayList::new));
                String additionalServices = String.join("  ", serviceNames);  // Join with two spaces

                String amountSpent = "";
                if (reservation.getStatus() == ReservationStatus.WAITING) {
                    amountSpent = "In Progress";
                } else if (reservation.getStatus() == ReservationStatus.REJECTED || reservation.getStatus() == ReservationStatus.CHANGED_MIND) {
                    amountSpent = "X";
                } else {
                    amountSpent = String.valueOf(reservation.getTotalCost());
                    totalCost += reservation.getTotalCost();
                }

                Object[] row = {
                    reservation.getReservationId(),
                    reservation.getRoomId(),
                    roomTypeName,
                    reservation.getCheckInDate(),
                    reservation.getCheckOutDate(),
                    reservation.getStatus(),
                    additionalServices,
                    reservation.getTotalCost(),
                    amountSpent
                };
                model.addRow(row);
            }
            reservationsTable.setModel(model);
            configureAdditionalServicesColumn();
            add(scrollPane, BorderLayout.CENTER);
        }

        totalCostLabel.setText(String.format("Total Cost: %.2f", totalCost));

        revalidate();
        repaint();
    }

    private void configureAdditionalServicesColumn() {
        TableColumn additionalServicesColumn = reservationsTable.getColumnModel().getColumn(6);
        additionalServicesColumn.setCellEditor(new JTextAreaCellEditor());
    }

    private void cancelReservation() {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int reservationId = (int) reservationsTable.getValueAt(selectedRow, 0);
            ReservationController reservationController = ReservationController.getInstance();
            Reservation reservation = reservationController.findReservationById(reservationId);
            if (reservation != null) {
                if (reservation.getStatus() == ReservationStatus.WAITING) {
                    reservation.setStatus(ReservationStatus.CHANGED_MIND);
                    logReservation("src/com/hotelmanagement/data/changed_mind_reservations_by_period.csv", reservation);
                    JOptionPane.showMessageDialog(this, "Reservation status updated to CHANGED_MIND.");
                } else if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
                    reservation.setStatus(ReservationStatus.CANCELLED);
                    RoomController roomController = RoomController.getInstance();
                    Room room = roomController.findRoomById(reservation.getRoomId());
                    if (room != null) {
                        room.removeCheckInDate(reservation.getCheckInDate());
                        room.removeCheckOutDate(reservation.getCheckOutDate());
                        roomController.updateRoomAttributes(room);
                        roomController.saveRoomsToFile(); // Ensure the room status is saved
                    }
                    reservation.setRoomId(-1); // Set the room ID to -1
                    logReservation("src/com/hotelmanagement/data/canceled_reservations_by_period.csv", reservation);
                    JOptionPane.showMessageDialog(this, "Reservation status updated to CANCELLED.");
                } else {
                    JOptionPane.showMessageDialog(this, "Reservation cannot be cancelled.");
                    return;
                }
                reservationController.updateReservation(reservation);
                updateReservationsTable();
            } else {
                JOptionPane.showMessageDialog(this, "Reservation not found.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a reservation to cancel.");
        }
    }

    private void logReservation(String fileName, Reservation reservation) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.format("%d,%d,%s\n", guestId, reservation.getReservationId(), LocalDate.now().toString()));
            writer.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error logging reservation: " + e.getMessage());
        }
    }

    private void editReservation() {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int reservationId = (int) reservationsTable.getValueAt(selectedRow, 0);
            ReservationController reservationController = ReservationController.getInstance();
            Reservation reservation = reservationController.findReservationById(reservationId);
            if (reservation != null && reservation.getStatus() == ReservationStatus.WAITING) {
                EditReservationDialog editDialog = new EditReservationDialog(reservation);
                editDialog.setVisible(true);
                if (editDialog.isConfirmed()) {
                    Reservation updatedReservation = editDialog.getUpdatedReservation();
                    reservationController.updateReservation(updatedReservation);
                    updateReservationsTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Only reservations with status WAITING can be edited.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a reservation to edit.");
        }
    }

    private class EditReservationDialog extends JDialog {
        private Reservation reservation;
        private JTextField checkInField;
        private JTextField checkOutField;
        private JComboBox<String> roomTypeComboBox;
        private ArrayList<JCheckBox> serviceCheckBoxes;
        private boolean confirmed = false;

        public EditReservationDialog(Reservation reservation) {
            this.reservation = reservation;
            setTitle("Edit Reservation");
            setSize(400, 300);
            setLocationRelativeTo(null);
            setModal(true);
            setLayout(new BorderLayout());

            JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            fieldsPanel.add(new JLabel("Check-In Date (YYYY-MM-DD):"));
            checkInField = new JTextField(reservation.getCheckInDate().toString());
            fieldsPanel.add(checkInField);

            fieldsPanel.add(new JLabel("Check-Out Date (YYYY-MM-DD):"));
            checkOutField = new JTextField(reservation.getCheckOutDate().toString());
            fieldsPanel.add(checkOutField);

            fieldsPanel.add(new JLabel("Room Type:"));
            roomTypeComboBox = new JComboBox<>();
            ArrayList<RoomType> roomTypes = RoomTypeController.getInstance().getRoomTypeList();
            for (RoomType roomType : roomTypes) {
                roomTypeComboBox.addItem(roomType.getCategory().name());
                if (roomType.getRoomTypeId() == reservation.getRoomTypeId()) {
                    roomTypeComboBox.setSelectedItem(roomType.getCategory().name());
                }
            }
            fieldsPanel.add(roomTypeComboBox);

            fieldsPanel.add(new JLabel("Additional Services:"));
            JPanel servicesPanel = new JPanel();
            servicesPanel.setLayout(new BoxLayout(servicesPanel, BoxLayout.Y_AXIS));
            serviceCheckBoxes = new ArrayList<>();
            ArrayList<AdditionalServices> services = AdditionalServicesController.getInstance().getAdditionalServicesList();
            for (AdditionalServices service : services) {
                JCheckBox checkBox = new JCheckBox(service.getServiceName());
                if (reservation.getAdditionalServiceIds().contains(service.getServiceId())) {
                    checkBox.setSelected(true);
                }
                serviceCheckBoxes.add(checkBox);
                servicesPanel.add(checkBox);
            }
            JScrollPane servicesScrollPane = new JScrollPane(servicesPanel);
            fieldsPanel.add(servicesScrollPane);

            add(fieldsPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            JButton confirmButton = new JButton("Confirm");
            JButton cancelButton = new JButton("Cancel");
            buttonPanel.add(confirmButton);
            buttonPanel.add(cancelButton);
            add(buttonPanel, BorderLayout.SOUTH);

            confirmButton.addActionListener(e -> {
                try {
                    LocalDate checkInDate = LocalDate.parse(checkInField.getText());
                    LocalDate checkOutDate = LocalDate.parse(checkOutField.getText());

                    if (checkOutDate.isBefore(checkInDate)) {
                        JOptionPane.showMessageDialog(EditReservationDialog.this, "Check-Out date cannot be before Check-In date.");
                        return;
                    }

                    RoomType selectedRoomType = roomTypes.get(roomTypeComboBox.getSelectedIndex());
                    ArrayList<Integer> selectedServices = serviceCheckBoxes.stream()
                            .filter(JCheckBox::isSelected)
                            .map(cb -> services.get(serviceCheckBoxes.indexOf(cb)).getServiceId())
                            .collect(Collectors.toCollection(ArrayList::new));

                    reservation.setCheckInDate(checkInDate);
                    reservation.setCheckOutDate(checkOutDate);
                    reservation.setRoomTypeId(selectedRoomType.getRoomTypeId());
                    reservation.setAdditionalServiceIds(selectedServices);
                    confirmed = true;
                    setVisible(false);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(EditReservationDialog.this, "Invalid date format. Please use YYYY-MM-DD.");
                }
            });

            cancelButton.addActionListener(e -> setVisible(false));
        }

        public boolean isConfirmed() {
            return confirmed;
        }

        public Reservation getUpdatedReservation() {
            return reservation;
        }
    }
}
