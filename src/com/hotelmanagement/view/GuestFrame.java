package com.hotelmanagement.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuestFrame extends JFrame {

    private int guestId;

    public GuestFrame(int guestId) {
        this.guestId = guestId;
        setTitle("Guest Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        ReservationsPanel reservationsPanel = new ReservationsPanel(guestId);
        tabbedPane.addTab("Available Room Types", new AvailableRoomTypesPanel());
        tabbedPane.addTab("My Reservations", reservationsPanel);
        tabbedPane.addTab("Add Reservation", new AddReservationPanel(guestId, reservationsPanel));

        add(tabbedPane, BorderLayout.CENTER);

        // Create menu bar and add options
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem viewProfileMenuItem = new JMenuItem("View Profile");
        JMenuItem editProfileMenuItem = new JMenuItem("Edit Profile");
        JMenuItem signOutMenuItem = new JMenuItem("Sign Out");

        menu.add(viewProfileMenuItem);
        menu.add(editProfileMenuItem);
        menu.add(signOutMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Add action listener for sign out
        signOutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new LoginFrame().setVisible(true);
                });
            }
        });

        // Add action listener for view profile
        viewProfileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GuestFrame.this, new ViewProfilePanel(guestId), "View Profile", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add action listener for edit profile
        editProfileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GuestFrame.this, new EditProfilePanel(guestId), "Edit Profile", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        // Example guest ID for demonstration purposes
        int exampleGuestId = 1001;
        SwingUtilities.invokeLater(() -> {
            new GuestFrame(exampleGuestId).setVisible(true);
        });
    }
}
