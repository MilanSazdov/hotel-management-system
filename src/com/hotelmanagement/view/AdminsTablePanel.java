package com.hotelmanagement.view;

import com.hotelmanagement.controller.AdminController;
import com.hotelmanagement.model.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AdminsTablePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable adminTable;
    private DefaultTableModel adminTableModel;

    public AdminsTablePanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Admins");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        adminTableModel = new DefaultTableModel(new String[]{
            "ID", "Username", "First Name", "Last Name", "Phone Number", "Password", "Gender", "Birth Date", "Experience", "Salary", "Professional Qualifications"
        }, 0);
        adminTable = new JTable(adminTableModel);
        adminTable.setFillsViewportHeight(true);
        adminTable.setFont(new Font("Arial", Font.PLAIN, 14));
        adminTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(adminTable);
        add(scrollPane, BorderLayout.CENTER);

        updateAdminTable();
    }

    private void updateAdminTable() {
        adminTableModel.setRowCount(0); // Clear existing rows

        ArrayList<Admin> admins = AdminController.getInstance().getAdminList();
        for (Admin admin : admins) {
            Object[] row = {
                admin.getAdminId(),
                admin.getUsername(),
                admin.getName(),
                admin.getLastName(),
                admin.getPhoneNumber(),
                admin.getPassword(),
                admin.getGender(),
                admin.getBirthDate(),
                admin.getWorkingExperience(),
                admin.getSalary(),
                admin.getProfessionalQualification()
            };
            adminTableModel.addRow(row);
        }
    }
}
