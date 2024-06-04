package com.hotelmanagement.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextPane;

import com.hotelmanagement.controller.AdminController;
import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.controller.ReceptionistController;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.JLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener {
    private JTextField userText;
    private JPasswordField passwordText;
    private JButton loginButton;
    
    private AdminController adminController;
    private MaidController maidController;
    private ReceptionistController receptionistController;
    private GuestController guestController;
    
    
    public Login() {
    	
    	// Initialize the controllers
    	adminController = AdminController.getInstance();
    	maidController = MaidController.getInstance();
    	receptionistController = ReceptionistController.getInstance();
    	guestController = GuestController.getInstance();
    	
    	
        // Set up the frame
        setTitle("Login");
        setSize(1280, 720);  // Updated size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);  // Set background color

        // Panel to hold components
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        // User Name label and text field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        userLabel.setBounds(516, 106, 158, 80);
        panel.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(419, 196, 341, 48);
        panel.add(userText);

        // Password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        passwordLabel.setBounds(516, 269, 213, 80);
        panel.add(passwordLabel);

        passwordText = new JPasswordField(20);
        passwordText.setBounds(419, 360, 341, 48);
        panel.add(passwordText);

        // Login button
        loginButton = new JButton("Sign In");
        loginButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 30));
        loginButton.setBounds(419, 472, 341, 67);
        loginButton.addActionListener(this);
        panel.add(loginButton);

        // Add the panel to the frame
        getContentPane().add(panel);

        // Set visible
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String userName = userText.getText();
            String password = new String(passwordText.getPassword());
            
            if (adminController.checkUserExists(userName, password)) {
                // Open the admin frame
                // new AdminFrame();
            	System.out.println("Admin signed in");
            } else if (maidController.checkUserExists(userName, password)) {
                // Open the maid frame
                // new MaidFrame();
            	System.out.println("Maid signed in");
            } else if (receptionistController.checkUserExists(userName, password)) {
                // Open the receptionist frame
                // new ReceptionistFrame();
            	System.out.println("Receptionist signed in");
            } else if (guestController.checkUserExists(userName, password)) {
                // Open the guest frame
                // new GuestFrame();
            	System.out.println("Guest signed in");
            } else {
                System.out.println("User does not exist");
            }
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
