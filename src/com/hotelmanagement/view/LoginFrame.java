package com.hotelmanagement.view;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Hotel Management System - Login");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        LogoPanel logoPanel = new LogoPanel();
        logoPanel.setPreferredSize(new Dimension(400, 400));
        add(logoPanel, BorderLayout.WEST);

        LoginFormPanel loginFormPanel = new LoginFormPanel();
        add(loginFormPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
