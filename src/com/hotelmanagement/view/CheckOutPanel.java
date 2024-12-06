package com.hotelmanagement.view;

import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.Maid;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CheckOutPanel extends JPanel {

    private JTextField usernameField;
    private JTable reservationsTable;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JLabel noReservationsLabel;
    private Guest currentGuest;
    private int receptionistId;

    public CheckOutPanel(int receptionistId) {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel usernameLabel = new JLabel("Enter Guest Username:");
        usernameField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(searchButton);

        add(inputPanel, BorderLayout.NORTH);

        reservationsTable = new JTable();
        reservationsTable.setFillsViewportHeight(true);
        reservationsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        reservationsTable.setRowHeight(30);
        scrollPane = new JScrollPane(reservationsTable);
        add(scrollPane, BorderLayout.CENTER);

        noReservationsLabel = new JLabel("No reservations found for the current date.", SwingConstants.CENTER);
        noReservationsLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton checkOutButton = new JButton("Check Out");

        buttonPanel.add(checkOutButton);

        add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                currentGuest = GuestController.getInstance().getGuestByEmail(username);
                if (currentGuest != null) {
                    updateReservationsTable(currentGuest.getGuestId());
                } else {
                    JOptionPane.showMessageDialog(CheckOutPanel.this, "Guest not found.");
                }
            }
        });

        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = reservationsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int reservationId = (int) reservationsTable.getValueAt(selectedRow, 0);
                    checkOutReservation(reservationId);
                } else {
                    JOptionPane.showMessageDialog(CheckOutPanel.this, "Please select a reservation to check out.");
                }
            }
        });
    }

    public void updateReservationsTable(int guestId) {
        String[] columnNames = {"Reservation ID", "Room Type", "Check-In Date", "Check-Out Date", "Total Cost"};
        model = new DefaultTableModel(columnNames, 0);

        ReservationController reservationController = ReservationController.getInstance();
        List<Reservation> reservations = reservationController.getReservationsByGuestId(guestId)
                .stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.CHECKED_IN)
                .collect(Collectors.toList());

        if (reservations.isEmpty()) {
            reservationsTable.setModel(new DefaultTableModel());
            remove(scrollPane);
            add(noReservationsLabel, BorderLayout.CENTER);
        } else {
            remove(noReservationsLabel);
            for (Reservation reservation : reservations) {
                Object[] row = {
                    reservation.getReservationId(),
                    RoomTypeController.getInstance().searchRoomTypeById(reservation.getRoomTypeId()).getCategory().name(),
                    reservation.getCheckInDate(),
                    reservation.getCheckOutDate(),
                    reservation.getTotalCost()
                };
                model.addRow(row);
            }
            reservationsTable.setModel(model);
            add(scrollPane, BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }

    private void checkOutReservation(int reservationId) {
        ReservationController reservationController = ReservationController.getInstance();
        RoomController roomController = RoomController.getInstance();

        Reservation reservation = reservationController.findReservationById(reservationId);
        if (reservation != null && reservation.getStatus() == ReservationStatus.CHECKED_IN) {
            Room room = roomController.findRoomById(reservation.getRoomId());
            if (room != null) {
                room.setStatus(RoomStatus.CLEANING_PROCESS);
                room.removeCheckInDate(reservation.getCheckInDate());
                room.removeCheckOutDate(reservation.getCheckOutDate());
                roomController.updateRoomAttributes(room);
                roomController.saveRoomsToFile();

                Maid assignedMaid = assignRoomToMaid(room.getRoomId());
                if (assignedMaid != null) {
                    assignedMaid.addRoomId(room.getRoomId());
                    MaidController.getInstance().updateMaid(assignedMaid);
                    JOptionPane.showMessageDialog(this, "Check-out successful. The room has been assigned to Maid: " + assignedMaid.getUsername());
                }
            }

            reservation.setStatus(ReservationStatus.CHECKED_OUT);
            reservation.setRoomId(-1);
            reservationController.updateReservation(reservation);
            reservationController.saveReservationsToFile();
            
            writeCheckedOutReservationToFile(reservationId);

            updateReservationsTable(currentGuest.getGuestId());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to check out the reservation.");
        }
    }

    private Maid assignRoomToMaid(int roomId) {
        MaidController maidController = MaidController.getInstance();
        List<Maid> maids = maidController.getAllMaids();

        // Sort maids by the number of rooms they have to clean today
        maids.sort(Comparator.comparingInt(maid -> maid.getRoomsId().size()));

        return maids.isEmpty() ? null : maids.get(0);
    }
    
    private void writeCheckedOutReservationToFile(int reservationId) {
        String path = "src/com/hotelmanagement/data/checked_out_reservations_by_period.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(reservationId + "," + receptionistId + "," + LocalDate.now() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
