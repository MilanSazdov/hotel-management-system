package com.hotelmanagement.view;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.hotelmanagement.controller.AdminController;
import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.controller.ReceptionistController;
import com.hotelmanagement.model.Admin;
import com.hotelmanagement.model.Gender;
import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.Maid;
import com.hotelmanagement.model.Receptionist;

public class EditProfileDialogMaid extends JDialog {

    public EditProfileDialogMaid(Frame owner, int maidId) {
        super(owner, "Edit Profile", true);
        setLayout(new GridBagLayout());
        setSize(400, 400);
        setLocationRelativeTo(owner);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        Maid maid = MaidController.getInstance().getMaidById(maidId);
        if (maid == null) {
            add(new JLabel("Maid not found"));
            return;
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        JTextField usernameField = new JTextField(maid.getUsername(), 20);
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Name:"), gbc);
        JTextField nameField = new JTextField(maid.getName(), 20);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Last Name:"), gbc);
        JTextField lastNameField = new JTextField(maid.getLastName(), 20);
        gbc.gridx = 1;
        add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Gender:"), gbc);
        JComboBox<Gender> genderComboBox = new JComboBox<>(Gender.values());
        genderComboBox.setSelectedItem(maid.getGender());
        gbc.gridx = 1;
        add(genderComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Birthdate (YYYY-MM-DD):"), gbc);
        JTextField birthDateField = new JTextField(maid.getBirthDate().toString(), 20);
        gbc.gridx = 1;
        add(birthDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Phone:"), gbc);
        JTextField phoneField = new JTextField(maid.getPhoneNumber(), 20);
        gbc.gridx = 1;
        add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Password:"), gbc);
        JTextField passwordField = new JTextField(maid.getPassword(), 20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Save");
        add(saveButton, gbc);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String name = nameField.getText();
                String lastName = lastNameField.getText();
                Gender gender = (Gender) genderComboBox.getSelectedItem();
                String birthDateStr = birthDateField.getText();
                String phoneNumber = phoneField.getText();
                String password = passwordField.getText();

                if (username.isEmpty() || name.isEmpty() || lastName.isEmpty() || birthDateStr.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(EditProfileDialogMaid.this, "All fields must be filled.");
                    return;
                }

                LocalDate birthDate;
                try {
                    birthDate = LocalDate.parse(birthDateStr);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(EditProfileDialogMaid.this, "Invalid birth date format.");
                    return;
                }

                if (birthDate.isAfter(LocalDate.now().minusYears(10))) {
                    JOptionPane.showMessageDialog(EditProfileDialogMaid.this, "Maid must be older than 10 years.");
                    return;
                }

                if (!phoneNumber.matches("\\d+")) {
                    JOptionPane.showMessageDialog(EditProfileDialogMaid.this, "Phone number must contain only digits.");
                    return;
                }

                if (!isUsernameUnique(username, maidId)) {
                    JOptionPane.showMessageDialog(EditProfileDialogMaid.this, "Username already exists.");
                    return;
                }

                maid.setUsername(username);
                maid.setName(name);
                maid.setLastName(lastName);
                maid.setGender(gender);
                maid.setBirthDate(birthDate);
                maid.setPhoneNumber(phoneNumber);
                maid.setPassword(password);
                MaidController.getInstance().saveMaidsToFile();
                JOptionPane.showMessageDialog(EditProfileDialogMaid.this, "Profile updated successfully!");
                dispose();
            }
        });
    }

    private boolean isUsernameUnique(String username, int maidId) {
        ArrayList<Receptionist> receptionists = ReceptionistController.getInstance().getAllReceptionists();
        for (Receptionist receptionist : receptionists) {
            if (receptionist.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }
        ArrayList<Admin> admins = AdminController.getInstance().getAllAdmins();
        for (Admin admin : admins) {
            if (admin.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }
        ArrayList<Maid> maids = MaidController.getInstance().getAllMaids();
        for (Maid maid : maids) {
            if (maid.getMaidId() != maidId && maid.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }
        ArrayList<Guest> guests = GuestController.getInstance().getAllGuests();
        for (Guest guest : guests) {
            if (guest.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }
        return true;
    }
}
