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

import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.controller.ReceptionistController;
import com.hotelmanagement.model.Maid;
import com.hotelmanagement.model.Receptionist;

public class ViewProfileDialogMaid extends JDialog {

	public ViewProfileDialogMaid(Frame owner, int maidId) {
        super(owner, "View Profile", true);
        setLayout(new GridBagLayout());
        setSize(400, 400);
        setLocationRelativeTo(owner);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        Maid maid = MaidController.getInstance().getMaidById(maidId);
        if (maid == null) {
            add(new JLabel("Receptionist not found"));
            return;
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(maid.getUsername()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(maid.getName()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(maid.getLastName()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(maid.getGender().toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Birthdate:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(maid.getBirthDate().toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(maid.getPhoneNumber()), gbc);


        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(maid.getPassword()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Working Experience:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(String.valueOf(maid.getWorkingExperience())), gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        add(new JLabel("Salary:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(String.format("%.2f", maid.getSalary())), gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        add(new JLabel("Professional Qualification:"), gbc);
        gbc.gridx = 1;
        add(new JLabel(maid.getProfessionalQualification().toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        add(okButton, gbc);
    }

}
