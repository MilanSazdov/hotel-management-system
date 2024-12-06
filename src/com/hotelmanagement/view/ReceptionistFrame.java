package com.hotelmanagement.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;

public class ReceptionistFrame extends JFrame {

    private int receptionistId;

    public ReceptionistFrame(int receptionistId) {
        this.receptionistId = receptionistId;
        setTitle("Receptionist Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Automatically reject reservations that are not confirmed by their start date
        autoRejectExpiredReservations();

        JTabbedPane tabbedPane = new JTabbedPane();

        // Add tabs for different functionalities
        tabbedPane.addTab("Reservations", new ReservationsPanelReceptionist());
        tabbedPane.addTab("Confirm Reservations", new ConfirmReservationsPanel(receptionistId));
        tabbedPane.addTab("Check In", new CheckInPanel(receptionistId));
        tabbedPane.addTab("Check Out", new CheckOutPanel(receptionistId));
        tabbedPane.addTab("Register New Guest", new RegisterNewGuestPanel());
        tabbedPane.addTab("Daily Activities", new DailyActivitiesPanel());
        tabbedPane.addTab("Guests", new GuestTablePanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Create menu bar and add sign out option
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        
        JMenuItem viewProfileMenuItem = new JMenuItem("View Profile");
        JMenuItem editProfileMenuItem = new JMenuItem("Edit Profile");
        JMenuItem signOutMenuItem = new JMenuItem("Sign Out");

        viewProfileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewProfileDialog(ReceptionistFrame.this, receptionistId).setVisible(true);
            }
        });

        editProfileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditProfileDialog(ReceptionistFrame.this, receptionistId).setVisible(true);
            }
        });

        signOutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new LoginFrame().setVisible(true);
                });
            }
        });

        menu.add(viewProfileMenuItem);
        menu.add(editProfileMenuItem);
        menu.add(signOutMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void autoRejectExpiredReservations() {
        LocalDate today = LocalDate.now();
        ReservationController reservationController = ReservationController.getInstance();
        List<Reservation> reservations = reservationController.getAllReservations();

        try (FileWriter writer = new FileWriter("src/com/hotelmanagement/data/rejected_reservations_by_period.csv", true)) {
            for (Reservation reservation : reservations) {
                if (reservation.getStatus() == ReservationStatus.WAITING && reservation.getCheckInDate().isBefore(today)) {
                    reservation.setStatus(ReservationStatus.REJECTED);
                    reservationController.updateReservation(reservation);

                    writer.append(String.format("%d,%d,%s\n", receptionistId, reservation.getReservationId(), LocalDate.now().toString()));
                }
            }
            writer.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to CSV file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        reservationController.saveReservationsToFile();
    }

    public static void main(String[] args) {
        int exampleReceptionistId = 1;
        SwingUtilities.invokeLater(() -> {
            new ReceptionistFrame(exampleReceptionistId).setVisible(true);
        });
    }
}
