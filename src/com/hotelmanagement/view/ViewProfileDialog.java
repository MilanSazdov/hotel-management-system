package com.hotelmanagement.view;

import com.hotelmanagement.controller.ReceptionistController;
import com.hotelmanagement.model.Receptionist;

import javax.swing.*;
import java.awt.*;

public class ViewProfileDialog extends JDialog {

    public ViewProfileDialog(Frame owner, int receptionistId) {
        super(owner, "View Profile", true);
        setLayout(new GridBagLayout());
        setSize(400, 400); // Adjusted size to accommodate additional fields
        setLocationRelativeTo(owner);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        Receptionist receptionist = ReceptionistController.getInstance().getReceptionistById(receptionistId);
        if (receptionist == null) {
            add(new JLabel("Receptionist not found"));
            return;
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(receptionist.getUsername()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(receptionist.getName()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(receptionist.getLastName()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(receptionist.getGender().toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Birthdate:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(receptionist.getBirthDate().toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(receptionist.getPhoneNumber()), gbc);


        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(receptionist.getPassword()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Working Experience:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(String.valueOf(receptionist.getWorkingExperience())), gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        add(new JLabel("Salary:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(String.format("%.2f", receptionist.getSalary())), gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        add(new JLabel("Professional Qualification:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(receptionist.getProfessionalQualification().toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        add(okButton, gbc);
    }
}
