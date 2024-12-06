package com.hotelmanagement.main;

import javax.swing.SwingUtilities;

import com.hotelmanagement.view.LoginFrame;

public class HotelManagementApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
