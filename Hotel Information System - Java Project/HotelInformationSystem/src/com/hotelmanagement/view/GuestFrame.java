package com.hotelmanagement.view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GuestFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuestFrame frame = new GuestFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GuestFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		// Create the menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// Add the "Reservations" menu
		JMenu menuReservations = new JMenu("Reservations");
		menuBar.add(menuReservations);

		// Add "My Reservations" menu item
		JMenuItem menuItemMyReservations = new JMenuItem("My Reservations");
		menuReservations.add(menuItemMyReservations);

		// Add "Make Reservation" menu item
		JMenuItem menuItemMakeReservation = new JMenuItem("Make Reservation");
		menuReservations.add(menuItemMakeReservation);
		
		
	}
}
