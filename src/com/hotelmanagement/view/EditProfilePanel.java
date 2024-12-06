package com.hotelmanagement.view;

import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.model.Gender;
import com.hotelmanagement.model.Guest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class EditProfilePanel extends JPanel {

    public EditProfilePanel(int guestId) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        Guest guest = GuestController.getInstance().getGuestById(guestId);
        if (guest == null) {
            add(new JLabel("Guest not found"));
            return;
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Name:"), gbc);
        JTextField nameField = new JTextField(guest.getName());
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Last Name:"), gbc);
        JTextField lastNameField = new JTextField(guest.getLastName());
        gbc.gridx = 1;
        add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Gender:"), gbc);
        JComboBox<Gender> genderComboBox = new JComboBox<>(Gender.values());
        genderComboBox.setSelectedItem(guest.getGender());
        gbc.gridx = 1;
        add(genderComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Birthdate (YYYY-MM-DD):"), gbc);
        JTextField birthDateField = new JTextField(guest.getBirthDate().toString());
        gbc.gridx = 1;
        add(birthDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Phone:"), gbc);
        JTextField phoneField = new JTextField(guest.getPhoneNumber());
        gbc.gridx = 1;
        add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Email:"), gbc);
        JTextField emailField = new JTextField(guest.getEmail());
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Passport Number:"), gbc);
        JTextField passportField = new JTextField(guest.getPassportNumber());
        gbc.gridx = 1;
        add(passportField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        JButton saveButton = new JButton("Save");
        add(saveButton, gbc);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String lastName = lastNameField.getText();
                Gender gender = (Gender) genderComboBox.getSelectedItem();
                String birthDateStr = birthDateField.getText();
                String phoneNumber = phoneField.getText();
                String email = emailField.getText();
                String passportNumber = passportField.getText();

                if (name.isEmpty() || lastName.isEmpty() || birthDateStr.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || passportNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(EditProfilePanel.this, "All fields must be filled.");
                    return;
                }

                LocalDate birthDate;
                try {
                    birthDate = LocalDate.parse(birthDateStr);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(EditProfilePanel.this, "Invalid birth date format.");
                    return;
                }

                if (birthDate.isAfter(LocalDate.now().minusYears(10))) {
                    JOptionPane.showMessageDialog(EditProfilePanel.this, "Guest must be older than 10 years.");
                    return;
                }

                if (!phoneNumber.matches("\\d+")) {
                    JOptionPane.showMessageDialog(EditProfilePanel.this, "Phone number must contain only digits.");
                    return;
                }

                if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    JOptionPane.showMessageDialog(EditProfilePanel.this, "Invalid email format.");
                    return;
                }

                if (!email.equalsIgnoreCase(guest.getEmail()) && !isEmailUnique(email)) {
                    JOptionPane.showMessageDialog(EditProfilePanel.this, "Email already exists.");
                    return;
                }

                if (!passportNumber.matches("^[a-zA-Z0-9]+$")) {
                    JOptionPane.showMessageDialog(EditProfilePanel.this, "Passport number must contain only letters and digits.");
                    return;
                }

                guest.setName(name);
                guest.setLastName(lastName);
                guest.setGender(gender);
                guest.setBirthDate(birthDate);
                guest.setPhoneNumber(phoneNumber);
                guest.setEmail(email);
                guest.setPassportNumber(passportNumber);
                guest.setUsername(email);
                GuestController.getInstance().saveGuestsToFile();
                JOptionPane.showMessageDialog(EditProfilePanel.this, "Profile updated successfully!");
            }
        });
    }

    private boolean isEmailUnique(String email) {
        ArrayList<Guest> guests = GuestController.getInstance().getAllGuests();
        for (Guest guest : guests) {
            if (guest.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }
}
