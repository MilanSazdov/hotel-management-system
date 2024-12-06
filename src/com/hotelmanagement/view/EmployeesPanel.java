package com.hotelmanagement.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import com.hotelmanagement.controller.AdminController;
import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.controller.ReceptionistController;
import com.hotelmanagement.model.*;

public class EmployeesPanel extends JPanel {

    private JTable receptionistTable;
    private DefaultTableModel receptionistTableModel;
    private JTable maidTable;
    private DefaultTableModel maidTableModel;

    public EmployeesPanel() {
        setLayout(new BorderLayout());

        JPanel tablesPanel = new JPanel(new GridLayout(2, 1));

        JPanel receptionistPanel = new JPanel(new BorderLayout());
        JLabel receptionistLabel = new JLabel("Receptionists");
        receptionistLabel.setHorizontalAlignment(SwingConstants.CENTER);
        receptionistPanel.add(receptionistLabel, BorderLayout.NORTH);

        receptionistTable = new JTable();
        receptionistTableModel = new DefaultTableModel(new String[]{
            "ID", "Role", "Name", "Last Name", "Gender", "Birthdate", "Phone Number", "Username", "Password", "Experience", "Qualification", "Salary"
        }, 0);
        receptionistTable.setModel(receptionistTableModel);
        JScrollPane receptionistScrollPane = new JScrollPane(receptionistTable);
        receptionistPanel.add(receptionistScrollPane, BorderLayout.CENTER);

        JPanel maidPanel = new JPanel(new BorderLayout());
        JLabel maidLabel = new JLabel("Maids");
        maidLabel.setHorizontalAlignment(SwingConstants.CENTER);
        maidPanel.add(maidLabel, BorderLayout.NORTH);

        maidTable = new JTable();
        maidTableModel = new DefaultTableModel(new String[]{
            "ID", "Role", "Name", "Last Name", "Gender", "Birthdate", "Phone Number", "Username", "Password", "Experience", "Qualification", "Salary", "Rooms"
        }, 0);
        maidTable.setModel(maidTableModel);
        JScrollPane maidScrollPane = new JScrollPane(maidTable);
        maidPanel.add(maidScrollPane, BorderLayout.CENTER);

        tablesPanel.add(receptionistPanel);
        tablesPanel.add(maidPanel);

        // Adding buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add New Employee");
        JButton deleteButton = new JButton("Delete Employee");
        JButton editButton = new JButton("Edit Employee");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        add(tablesPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data
        updateEmployeeTables();

        // Adding action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewEmployee();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editEmployee();
            }
        });
    }

    private void updateEmployeeTables() {
        receptionistTableModel.setRowCount(0); // Clear existing rows
        maidTableModel.setRowCount(0); // Clear existing rows

        ArrayList<Receptionist> receptionists = ReceptionistController.getInstance().getReceptionistList();
        for (Receptionist receptionist : receptionists) {
            Object[] row = {
                receptionist.getReceptionistId(),
                "Receptionist",
                receptionist.getName(),
                receptionist.getLastName(),
                receptionist.getGender(),
                receptionist.getBirthDate(),
                receptionist.getPhoneNumber(),
                receptionist.getUsername(),
                receptionist.getPassword(),
                receptionist.getWorkingExperience(),
                receptionist.getProfessionalQualification(),
                receptionist.getSalary()
            };
            receptionistTableModel.addRow(row);
        }

        ArrayList<Maid> maids = MaidController.getInstance().getAllMaids();
        for (Maid maid : maids) {
            Object[] row = {
                maid.getMaidId(),
                "Maid",
                maid.getName(),
                maid.getLastName(),
                maid.getGender(),
                maid.getBirthDate(),
                maid.getPhoneNumber(),
                maid.getUsername(),
                maid.getPassword(),
                maid.getWorkingExperience(),
                maid.getProfessionalQualification(),
                maid.getSalary(),
                maid.getRoomsId().size() + " Rooms: " + maid.getRoomsId().toString()
            };
            maidTableModel.addRow(row);
        }
    }

    private void addNewEmployee() {
        JTextField nameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JComboBox<Gender> genderComboBox = new JComboBox<>(Gender.values());
        JTextField birthDateField = new JTextField();
        JTextField phoneNumberField = new JTextField();
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField experienceField = new JTextField();
        JComboBox<ProfessionalQualification> qualificationComboBox = new JComboBox<>(ProfessionalQualification.values());
        JComboBox<String> roleComboBox = new JComboBox<>(new String[] {"Receptionist", "Maid"});

        Object[] message = {
            "Role:", roleComboBox,
            "Name:", nameField,
            "Last Name:", lastNameField,
            "Gender:", genderComboBox,
            "Birthdate (yyyy-mm-dd):", birthDateField,
            "Phone Number:", phoneNumberField,
            "Username:", usernameField,
            "Password:", passwordField,
            "Experience:", experienceField,
            "Qualification:", qualificationComboBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Employee", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String role = roleComboBox.getSelectedItem().toString();
                String name = nameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                Gender gender = (Gender) genderComboBox.getSelectedItem();
                String birthDateStr = birthDateField.getText().trim();
                String phoneNumber = phoneNumberField.getText().trim();
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                String experienceStr = experienceField.getText().trim();
                ProfessionalQualification qualification = (ProfessionalQualification) qualificationComboBox.getSelectedItem();

                // Validate input
                if (name.isEmpty() || lastName.isEmpty() || birthDateStr.isEmpty() || phoneNumber.isEmpty() ||
                    username.isEmpty() || password.isEmpty() || experienceStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate birthDate;
                try {
                    birthDate = LocalDate.parse(birthDateStr);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid birthdate format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (birthDate.isAfter(LocalDate.now().minusYears(10))) {
                    JOptionPane.showMessageDialog(this, "Employee must be older than 10 years.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!phoneNumber.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Phone number must contain only digits.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (AdminController.getInstance().getAdminByUsername(username) != null || 
                    ReceptionistController.getInstance().getReceptionistByUsername(username) != null ||
                    MaidController.getInstance().getMaidByUsername(username) != null ||
                    GuestController.getInstance().getGuestByEmail(username) != null) {
                    JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int experience;
                try {
                    experience = Integer.parseInt(experienceStr);
                    if (experience <= 0) {
                        JOptionPane.showMessageDialog(this, "Experience must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Experience must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (role.equals("Receptionist")) {
                    int newId = getMaxReceptionistId() + 1;
                    Receptionist newReceptionist = new Receptionist(newId, name, lastName, gender, birthDate, phoneNumber, username, password, experience, 0.0, qualification);
                    ReceptionistController.getInstance().addReceptionist(newReceptionist);
                } else if (role.equals("Maid")) {
                    int newId = getMaxMaidId() + 1;
                    Maid newMaid = new Maid(newId, name, lastName, gender, birthDate, phoneNumber, username, password, experience, 0.0, qualification, new ArrayList<>());
                    MaidController.getInstance().addMaid(newMaid);
                }

                updateEmployeeTables();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void deleteEmployee() {
        int selectedRow = receptionistTable.getSelectedRow();
        if (selectedRow >= 0) {
            int employeeId = (int) receptionistTable.getValueAt(selectedRow, 0);
            Receptionist receptionist = ReceptionistController.getInstance().getReceptionistById(employeeId);
            if (receptionist != null) {
                ReceptionistController.getInstance().removeReceptionist(receptionist);
                updateEmployeeTables();
            } else {
                JOptionPane.showMessageDialog(this, "Receptionist not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            selectedRow = maidTable.getSelectedRow();
            if (selectedRow >= 0) {
                int employeeId = (int) maidTable.getValueAt(selectedRow, 0);
                Maid maid = MaidController.getInstance().getMaidById(employeeId);
                if (maid != null) {
                	if (maid.getRoomsId().size() > 0) {
                		JOptionPane.showMessageDialog(this, "Maid cannot be deleted as she has assigned rooms.", "Error", JOptionPane.ERROR_MESSAGE);
                	}else {
                		MaidController.getInstance().removeMaid(maid);
                		updateEmployeeTables();
                	}
                } else {
                    JOptionPane.showMessageDialog(this, "Maid not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No employee selected.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editEmployee() {
        int selectedRow = receptionistTable.getSelectedRow();
        String role;
        int employeeId;
        Staff employee;

        if (selectedRow >= 0) {
            role = (String) receptionistTable.getValueAt(selectedRow, 1);
            employeeId = (int) receptionistTable.getValueAt(selectedRow, 0);
            employee = ReceptionistController.getInstance().getReceptionistById(employeeId);
        } else {
            selectedRow = maidTable.getSelectedRow();
            if (selectedRow >= 0) {
                role = (String) maidTable.getValueAt(selectedRow, 1);
                employeeId = (int) maidTable.getValueAt(selectedRow, 0);
                employee = MaidController.getInstance().getMaidById(employeeId);
            } else {
                JOptionPane.showMessageDialog(this, "No employee selected.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        JTextField nameField = new JTextField(employee.getName());
        JTextField lastNameField = new JTextField(employee.getLastName());
        JComboBox<Gender> genderComboBox = new JComboBox<>(Gender.values());
        genderComboBox.setSelectedItem(employee.getGender());
        JTextField birthDateField = new JTextField(employee.getBirthDate().toString());
        JTextField phoneNumberField = new JTextField(employee.getPhoneNumber());
        JTextField usernameField = new JTextField(employee.getUsername());
        JTextField passwordField = new JTextField(employee.getPassword());
        JTextField experienceField = new JTextField(String.valueOf(employee.getWorkingExperience()));
        JComboBox<ProfessionalQualification> qualificationComboBox = new JComboBox<>(ProfessionalQualification.values());
        qualificationComboBox.setSelectedItem(employee.getProfessionalQualification());

        Object[] message = {
            "Role:", role,
            "Name:", nameField,
            "Last Name:", lastNameField,
            "Gender:", genderComboBox,
            "Birthdate (yyyy-mm-dd):", birthDateField,
            "Phone Number:", phoneNumberField,
            "Username:", usernameField,
            "Password:", passwordField,
            "Experience:", experienceField,
            "Qualification:", qualificationComboBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Employee", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                Gender gender = (Gender) genderComboBox.getSelectedItem();
                String birthDateStr = birthDateField.getText().trim();
                String phoneNumber = phoneNumberField.getText().trim();
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                String experienceStr = experienceField.getText().trim();
                ProfessionalQualification qualification = (ProfessionalQualification) qualificationComboBox.getSelectedItem();

                // Validate input
                if (name.isEmpty() || lastName.isEmpty() || birthDateStr.isEmpty() || phoneNumber.isEmpty() ||
                    username.isEmpty() || password.isEmpty() || experienceStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate birthDate;
                try {
                    birthDate = LocalDate.parse(birthDateStr);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid birthdate format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (birthDate.isAfter(LocalDate.now().minusYears(10))) {
                    JOptionPane.showMessageDialog(this, "Employee must be older than 10 years.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!phoneNumber.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Phone number must contain only digits.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!employee.getUsername().equals(username) &&
                    (AdminController.getInstance().getAdminByUsername(username) != null || 
                     ReceptionistController.getInstance().getReceptionistByUsername(username) != null ||
                     MaidController.getInstance().getMaidByUsername(username) != null ||
                     GuestController.getInstance().getGuestByEmail(username) != null)) {
                    JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int experience;
                try {
                    experience = Integer.parseInt(experienceStr);
                    if (experience <= 0) {
                        JOptionPane.showMessageDialog(this, "Experience must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Experience must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                employee.setName(name);
                employee.setLastName(lastName);
                employee.setGender(gender);
                employee.setBirthDate(birthDate);
                employee.setPhoneNumber(phoneNumber);
                employee.setUsername(username);
                employee.setPassword(password);
                employee.setWorkingExperience(experience);
                employee.setProfessionalQualification(qualification);

                // Save the changes
                if (role.equals("Receptionist")) {
                    ReceptionistController.getInstance().updateReceptionist((Receptionist) employee);
                } else if (role.equals("Maid")) {
                    MaidController.getInstance().updateMaid((Maid) employee);
                }

                updateEmployeeTables();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private int getMaxReceptionistId() {
        int maxId = 0;
        ArrayList<Receptionist> receptionists = ReceptionistController.getInstance().getReceptionistList();
        for (Receptionist receptionist : receptionists) {
            if (receptionist.getReceptionistId() > maxId) {
                maxId = receptionist.getReceptionistId();
            }
        }
        return maxId;
    }

    private int getMaxMaidId() {
        int maxId = 0;
        ArrayList<Maid> maids = MaidController.getInstance().getAllMaids();
        for (Maid maid : maids) {
            if (maid.getMaidId() > maxId) {
                maxId = maid.getMaidId();
            }
        }
        return maxId;
    }
}
