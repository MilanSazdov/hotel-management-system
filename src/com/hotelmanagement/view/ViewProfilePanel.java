package com.hotelmanagement.view;

import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.model.Guest;

import javax.swing.*;
import java.awt.*;

public class ViewProfilePanel extends JPanel {

    public ViewProfilePanel(int guestId) {
        setLayout(new BorderLayout());

        Guest guest = GuestController.getInstance().getGuestById(guestId);
        if (guest == null) {
            add(new JLabel("Guest not found"), BorderLayout.CENTER);
            return;
        }

        JTextArea profileArea = new JTextArea();
        profileArea.setText(
                            "Name: " + guest.getName() + "\n" +
                            "Last Name: " + guest.getLastName() + "\n" +
                            "Gender: " + guest.getGender() + "\n" +
                            "Birthdate: " + guest.getBirthDate() + "\n" +
                            "Phone: " + guest.getPhoneNumber() + "\n" +
                            "Email: " + guest.getEmail() + "\n" +
                            "Passport Number: " + guest.getPassportNumber());
        profileArea.setEditable(false);

        add(new JScrollPane(profileArea), BorderLayout.CENTER);
    }
}
