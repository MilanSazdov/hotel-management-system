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

public class RegisterNewGuestPanel extends JPanel {

    private JTextField nameField;
    private JTextField lastNameField;
    private JComboBox<Gender> genderComboBox;
    private JTextField birthDateField;
    private JTextField phoneNumberField;
    private JTextField emailField;
    private JTextField passportNumberField;

    public RegisterNewGuestPanel() {
        setLayout(new GridLayout(9, 2, 10, 10));

        add(new JLabel("First Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        add(lastNameField);

        add(new JLabel("Gender:"));
        genderComboBox = new JComboBox<>(Gender.values());
        add(genderComboBox);

        add(new JLabel("Birth Date (YYYY-MM-DD):"));
        birthDateField = new JTextField();
        add(birthDateField);

        add(new JLabel("Phone Number:"));
        phoneNumberField = new JTextField();
        add(phoneNumberField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Passport Number:"));
        passportNumberField = new JTextField();
        add(passportNumberField);

        JButton registerButton = new JButton("Register");
        add(new JLabel());  // Empty label for spacing
        add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerGuest();
            }
        });
    }

    private void registerGuest() {
        String name = nameField.getText();
        String lastName = lastNameField.getText();
        Gender gender = (Gender) genderComboBox.getSelectedItem();
        String birthDateStr = birthDateField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String passportNumber = passportNumberField.getText();

        if (name.isEmpty() || lastName.isEmpty() || birthDateStr.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || passportNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }

        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(birthDateStr);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid birth date format.");
            return;
        }

        if (birthDate.isAfter(LocalDate.now().minusYears(10))) {
            JOptionPane.showMessageDialog(this, "Guest must be older than 10 years.");
            return;
        }

        if (!phoneNumber.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Phone number must contain only digits.");
            return;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return;
        }

        if (!isEmailUnique(email)) {
            JOptionPane.showMessageDialog(this, "Email already exists.");
            return;
        }

        if (!passportNumber.matches("^[a-zA-Z0-9]+$")) {
            JOptionPane.showMessageDialog(this, "Passport number must contain only letters and digits.");
            return;
        }

        int guestId = getNextGuestId();
        Guest newGuest = new Guest(guestId, name, lastName, gender, birthDate, phoneNumber, email, passportNumber, new ArrayList<>());

        GuestController.getInstance().addGuest(newGuest);
        GuestController.getInstance().appendGuestToFile(newGuest);

        JOptionPane.showMessageDialog(this, "Guest registered successfully.");
        clearFields();
    }

    private int getNextGuestId() {
        return getMaxGuestId() + 1;
    }

    private int getMaxGuestId() {
        int maxId = 0;
        ArrayList<Guest> guests = GuestController.getInstance().getAllGuests();
        for (Guest guest : guests) {
            if (guest.getGuestId() > maxId) {
                maxId = guest.getGuestId();
            }
        }
        return maxId;
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

    private void clearFields() {
        nameField.setText("");
        lastNameField.setText("");
        birthDateField.setText("");
        phoneNumberField.setText("");
        emailField.setText("");
        passportNumberField.setText("");
        genderComboBox.setSelectedIndex(0);
    }
}
