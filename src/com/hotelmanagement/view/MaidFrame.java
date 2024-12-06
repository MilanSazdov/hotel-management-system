package com.hotelmanagement.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MaidFrame extends JFrame {
    private int maidId;

    public MaidFrame(int maidId) {
        this.maidId = maidId;
        setTitle("Maid Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Clean Rooms", new CleanRoomsPanel(maidId));

        add(tabbedPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem viewProfileMenuItem = new JMenuItem("View Profile");
        JMenuItem editProfileMenuItem = new JMenuItem("Edit Profile");
        JMenuItem signOutMenuItem = new JMenuItem("Sign Out");

        menu.add(viewProfileMenuItem);
        menu.add(editProfileMenuItem);
        menu.add(signOutMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Action listener for view profile
        viewProfileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewProfileDialogMaid(MaidFrame.this, maidId).setVisible(true);
            }
        });

        // Action listener for edit profile
        editProfileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditProfileDialogMaid(MaidFrame.this, maidId).setVisible(true);
            }
        });

        // Action listener for sign out
        signOutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new LoginFrame().setVisible(true);
                });
            }
        });
    }

    public static void main(String[] args) {
        int exampleMaidId = 2001; // Example maid ID for demonstration purposes
        SwingUtilities.invokeLater(() -> {
            new MaidFrame(exampleMaidId).setVisible(true);
        });
    }
}
