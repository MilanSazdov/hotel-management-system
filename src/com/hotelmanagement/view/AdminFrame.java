package com.hotelmanagement.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminFrame extends JFrame {
	
	private int adminId;
	
    public AdminFrame(int adminId) {
        setTitle("Admin Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Employees", new EmployeesPanel());
        tabbedPane.addTab("Room Types", new RoomTypesPanel());
        tabbedPane.addTab("Price Lists", new PriceListsPanel());
        tabbedPane.addTab("Additional Services", new AdditionalServicesPanel());
        tabbedPane.addTab("Rooms", new RoomsPanel());
        tabbedPane.addTab("Income/Outcome", new ReportsPanel());
        tabbedPane.addTab("Report for Maids", new ReportForMaidsPanel());
        tabbedPane.addTab("Report for Reservations", new ReportForReservationsPanel());
        tabbedPane.addTab("Reports for Rooms", new ReportsForRoomsPanel());
        tabbedPane.addTab("Reservations Status Chart", new ReservationsStatusChartPanel());
        tabbedPane.addTab("Maid Busyness Chart", new MaidChartPanel());
        tabbedPane.addTab("Yearly Income Chart", new IncomeChartPanel());
        tabbedPane.addTab("Guests", new GuestTablePanel());
        tabbedPane.addTab("Admins", new AdminsTablePanel());

        add(tabbedPane, BorderLayout.CENTER);

     // Create menu bar and add sign out option
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem viewProfileMenuItem = new JMenuItem("View Profile");
        JMenuItem editProfileMenuItem = new JMenuItem("Edit Profile");
        JMenuItem signOutMenuItem = new JMenuItem("Sign Out");

        viewProfileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewProfileDialogAdmin(AdminFrame.this, adminId).setVisible(true);
            }
        });

        editProfileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditProfileDialogAdmin(AdminFrame.this, adminId).setVisible(true);
            }
        });

        signOutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new LoginFrame().setVisible(true);
                });
            }
        });

        menu.add(viewProfileMenuItem);
        menu.add(editProfileMenuItem);
        menu.add(signOutMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

}
