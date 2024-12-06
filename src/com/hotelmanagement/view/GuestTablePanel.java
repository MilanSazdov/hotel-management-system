package com.hotelmanagement.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.model.Guest;

public class GuestTablePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable guestTable;
    private DefaultTableModel guestTableModel;

    public GuestTablePanel() {
        setLayout(new BorderLayout());

        // Create and add the title label
        JLabel titleLabel = new JLabel("Guests", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        guestTableModel = new DefaultTableModel(new String[]{
            "ID", "First Name", "Last Name", "Gender", "Birth Date", "Phone Number", "Email", "Passport Number"
        }, 0);
        guestTable = new JTable(guestTableModel);
        JScrollPane scrollPane = new JScrollPane(guestTable);
        add(scrollPane, BorderLayout.CENTER);

        updateGuestTable();
    }

    private void updateGuestTable() {
        guestTableModel.setRowCount(0); // Clear existing rows
        ArrayList<Guest> guests = GuestController.getInstance().getAllGuests();
        for (Guest guest : guests) {
            Object[] row = {
                guest.getGuestId(),
                guest.getName(),
                guest.getLastName(),
                guest.getGender(),
                guest.getBirthDate(),
                guest.getPhoneNumber(),
                guest.getEmail(),
                guest.getPassportNumber()
            };
            guestTableModel.addRow(row);
        }
    }
}
