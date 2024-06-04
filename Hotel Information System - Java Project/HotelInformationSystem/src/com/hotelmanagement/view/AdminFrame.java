package com.hotelmanagement.view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.controller.ReceptionistController;
import com.hotelmanagement.model.Maid;
import com.hotelmanagement.model.Receptionist;

import java.util.List;

public class AdminFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    private JTable tableMaids;
    private JTable tableReceptionists;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AdminFrame frame = new AdminFrame();
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
    public AdminFrame() {
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1103, 658);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        setupMaidsTable();
        setupReceptionistsTable();
    }

    private void setupMaidsTable() {
        String[] columnNames = {"ID", "Name", "Last Name", "Gender", "Birth Date", "Phone", "Username", "Working Experience", "Salary", "Qualification"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tableMaids = new JTable(model);

        List<Maid> maids = MaidController.getInstance().getAllMaids();
        for (Maid maid : maids) {
            Object[] row = new Object[]{maid.getId(), maid.getName(), maid.getLastName(), maid.getGender(),
                                        maid.getBirthDate(), maid.getPhoneNumber(), maid.getUsername(),
                                        maid.getWorkingExperience(), maid.getSalary(), maid.getProfessionalQualification()};
            model.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(tableMaids);
        tabbedPane.addTab("Maids", null, scrollPane, "View all maids");
    }

    private void setupReceptionistsTable() {
        String[] columnNames = {"ID", "Name", "Last Name", "Gender", "Birth Date", "Phone", "Username", "Working Experience", "Salary", "Qualification"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tableReceptionists = new JTable(model);

        List<Receptionist> receptionists = ReceptionistController.getInstance().getAllReceptionists();
        for (Receptionist receptionist : receptionists) {
            Object[] row = new Object[]{receptionist.getId(), receptionist.getName(), receptionist.getLastName(), receptionist.getGender(),
                                        receptionist.getBirthDate(), receptionist.getPhoneNumber(), receptionist.getUsername(),
                                        receptionist.getWorkingExperience(), receptionist.getSalary(), receptionist.getProfessionalQualification()};
            model.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(tableReceptionists);
        tabbedPane.addTab("Receptionists", null, scrollPane, "View all receptionists");
    }
}
