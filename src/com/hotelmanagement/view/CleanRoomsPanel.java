package com.hotelmanagement.view;

import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomStatus;
import com.hotelmanagement.model.Maid;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CleanRoomsPanel extends JPanel {

    private JTable roomsTable;
    private DefaultTableModel model;
    private int maidId;

    public CleanRoomsPanel(int maidId) {
        this.maidId = maidId;
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Rooms to Clean", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        add(label, BorderLayout.NORTH);

        roomsTable = new JTable();
        roomsTable.setFillsViewportHeight(true);
        roomsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        roomsTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(roomsTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton cleanButton = new JButton("Clean Room");
        buttonPanel.add(cleanButton);
        add(buttonPanel, BorderLayout.SOUTH);

        cleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = roomsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int roomId = (int) roomsTable.getValueAt(selectedRow, 0);
                    cleanRoom(roomId);
                } else {
                    JOptionPane.showMessageDialog(CleanRoomsPanel.this, "Please select a room to clean.");
                }
            }
        });

        updateRoomsTable();
    }

    private void updateRoomsTable() {
        String[] columnNames = {"Room ID", "Room Number", "Room Type", "Status"};
        model = new DefaultTableModel(columnNames, 0);

        RoomController roomController = RoomController.getInstance();
        MaidController maidController = MaidController.getInstance();
        List<Integer> roomIds = maidController.getMaidById(maidId).getRoomsId();
        for (int roomId : roomIds) {
            Room room = roomController.findRoomById(roomId);
            if (room != null) {
                Object[] row = {
                    room.getRoomId(),
                    room.getRoomNumber(),
                    room.getRoomType().getCategory().name(),
                    room.getStatus().name()
                };
                model.addRow(row);
            }
        }
        roomsTable.setModel(model);
    }

    private void cleanRoom(int roomId) {
        RoomController roomController = RoomController.getInstance();
        Room room = roomController.findRoomById(roomId);
        if (room != null) {
            room.setStatus(RoomStatus.FREE);
            roomController.updateRoomAttributes(room);
            roomController.saveRoomsToFile();
            removeRoomFromMaid(roomId);
            logCleaningActivity(maidId, roomId, LocalDate.now());
            updateRoomsTable();
            JOptionPane.showMessageDialog(this, "Room cleaned successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to clean the room.");
        }
    }

    private void removeRoomFromMaid(int roomId) {
        MaidController maidController = MaidController.getInstance();
        Maid maid = maidController.getMaidById(maidId);
        ArrayList<Integer> roomsId = maid.getRoomsId();
        roomsId.remove(Integer.valueOf(roomId));
        maid.setRoomsId(roomsId);
        maidController.updateMaid(maid);
    }

    private void logCleaningActivity(int maidId, int roomId, LocalDate date) {
        String filePath = "src/com/hotelmanagement/data/cleaned_rooms_by_period.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(maidId + "," + roomId + "," + date);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
