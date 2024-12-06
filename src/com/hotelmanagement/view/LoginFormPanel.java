package com.hotelmanagement.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.hotelmanagement.controller.DataController;

public class LoginFormPanel extends JPanel {

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFormPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel loginLabel = new JLabel("LOGIN");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(loginLabel, gbc);

        JLabel emailLabel = new JLabel("Email");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(emailLabel, gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 102, 102));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = emailField.getText();
                String password = new String(passwordField.getPassword());

                DataController dataController = DataController.getInstance();
                String role = dataController.authenticateUser(username, password);

                if (role != null) {
                    int userId = dataController.getCurrentUserId();
                    switch (role) {
                        case "Admin":
                            new AdminFrame(userId).setVisible(true);
                            break;
                        case "Guest":
                            new GuestFrame(userId).setVisible(true);
                            break;
                        case "Receptionist":
                            new ReceptionistFrame(userId).setVisible(true);
                            break;
                        case "Maid":
                            new MaidFrame(userId).setVisible(true);
                            break;
                    }
                    SwingUtilities.getWindowAncestor(LoginFormPanel.this).dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginFormPanel.this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
