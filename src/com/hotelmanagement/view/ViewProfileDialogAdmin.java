package com.hotelmanagement.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.hotelmanagement.controller.AdminController;
import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.model.Admin;
import com.hotelmanagement.model.Maid;

public class ViewProfileDialogAdmin extends JDialog {

	public ViewProfileDialogAdmin(Frame owner, int adminId) {
        super(owner, "View Profile", true);
        setLayout(new GridBagLayout());
        setSize(400, 400);
        setLocationRelativeTo(owner);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        Admin admin = AdminController.getInstance().getAdminById(adminId);
        if (admin == null) {
            add(new JLabel("Receptionist not found"));
            return;
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(admin.getUsername()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(admin.getName()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(admin.getLastName()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(admin.getGender().toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Birthdate:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(admin.getBirthDate().toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(admin.getPhoneNumber()), gbc);


        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(admin.getPassword()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Working Experience:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(String.valueOf(admin.getWorkingExperience())), gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        add(new JLabel("Salary:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(String.format("%.2f", admin.getSalary())), gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        add(new JLabel("Professional Qualification:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(admin.getProfessionalQualification().toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        add(okButton, gbc);
    }

}
