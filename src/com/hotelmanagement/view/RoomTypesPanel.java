package com.hotelmanagement.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.model.RoomType;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomCategory;

public class RoomTypesPanel extends JPanel {

    private JTable roomTypeTable;
    private DefaultTableModel model;

    public RoomTypesPanel() {
        setLayout(new BorderLayout());

        // Panel for Room Types
        JPanel roomTypePanel = new JPanel(new BorderLayout());
        JLabel roomTypeLabel = new JLabel("Room Types");
        roomTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        roomTypePanel.add(roomTypeLabel, BorderLayout.NORTH);

        roomTypeTable = new JTable();
        updateRoomTypeTable();
        JScrollPane roomTypeScrollPane = new JScrollPane(roomTypeTable);
        roomTypePanel.add(roomTypeScrollPane, BorderLayout.CENTER);

        // Adding buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add New Room Type");
        JButton deleteButton = new JButton("Delete Room Type");
        JButton editButton = new JButton("Edit Room Type");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        add(roomTypePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Adding action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewRoomType();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRoomType();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editRoomType();
            }
        });
    }

    private void updateRoomTypeTable() {
        String[] columnNames = {
            "ID", "Number of Beds", "Category"
        };
        ArrayList<RoomType> roomTypes = RoomTypeController.getInstance().getRoomTypeList();
        model = new DefaultTableModel(columnNames, 0);

        for (RoomType roomType : roomTypes) {
            Object[] row = {
                roomType.getRoomTypeId(),
                roomType.getNumberOfBeds(),
                roomType.getCategory()
            };
            model.addRow(row);
        }

        roomTypeTable.setModel(model);
    }

    private void addNewRoomType() {
        Integer[] bedOptions = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        JComboBox<Integer> bedComboBox = new JComboBox<>(bedOptions);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Number of Beds:"));
        panel.add(bedComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Select Number of Beds", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            int selectedBeds = (int) bedComboBox.getSelectedItem();
            RoomCategory[] categories = getCategoriesByBeds(selectedBeds);
            JComboBox<RoomCategory> categoryComboBox = new JComboBox<>(categories);

            Object[] message = {
                "Number of Beds: " + selectedBeds,
                "Category:", categoryComboBox
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Add New Room Type", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String category = ((RoomCategory) categoryComboBox.getSelectedItem()).name();

                    // Check if room type already exists
                    ArrayList<RoomType> roomTypes = RoomTypeController.getInstance().getRoomTypeList();
                    for (RoomType roomType : roomTypes) {
                        if (roomType.getNumberOfBeds() == selectedBeds && roomType.getCategory().name().equalsIgnoreCase(category)) {
                            JOptionPane.showMessageDialog(this, "Room type already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    int newRoomTypeId = getMaxRoomTypeId() + 1;
                    RoomType newRoomType = new RoomType(newRoomTypeId, selectedBeds, RoomCategory.valueOf(category));
                    RoomTypeController.getInstance().addRoomType(newRoomType);
                    updateRoomTypeTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private RoomCategory[] getCategoriesByBeds(int numberOfBeds) {
        List<RoomCategory> categories = new ArrayList<>();
        switch (numberOfBeds) {
            case 1:
                categories.add(RoomCategory.SINGLE);
                break;
            case 2:
                categories.add(RoomCategory.DOUBLE_SINGLE_BED);
                categories.add(RoomCategory.DOUBLE_TWIN_BEDS);
                break;
            case 3:
                categories.add(RoomCategory.TRIPLE);
                categories.add(RoomCategory.TRIPLE_SINGLE);
                break;
            case 4:
                categories.add(RoomCategory.QUAD);
                categories.add(RoomCategory.QUAD_EXTRA);
                break;
            case 5:
                categories.add(RoomCategory.FIVE_BED);
                break;
            case 6:
                categories.add(RoomCategory.SIX_TWO_TRIPLE);
                categories.add(RoomCategory.SIX_THREE_DOUBLE);
                break;
            case 7:
                categories.add(RoomCategory.SEVEN);
                break;
            case 8:
                categories.add(RoomCategory.DELUXE);
                break;
            case 9:
                categories.add(RoomCategory.QUEEN);
                break;
            case 10:
                categories.add(RoomCategory.KING);
                break;
            default:
                break;
        }
        return categories.toArray(new RoomCategory[0]);
    }

    private void deleteRoomType() {
        int selectedRow = roomTypeTable.getSelectedRow();
        if (selectedRow >= 0) {
            int roomTypeId = (int) roomTypeTable.getValueAt(selectedRow, 0);

            // Check if room type is associated with any room
            ArrayList<Room> rooms = RoomController.getInstance().getRoomList();
            for (Room room : rooms) {
                if (room.getRoomType().getRoomTypeId() == roomTypeId) {
                    JOptionPane.showMessageDialog(this, "Cannot delete room type associated with a room.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            RoomType roomType = RoomTypeController.getInstance().searchRoomTypeById(roomTypeId);
            RoomTypeController.getInstance().removeRoomType(roomType);
            updateRoomTypeTable();
        } else {
            JOptionPane.showMessageDialog(this, "No room type selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editRoomType() {
        int selectedRow = roomTypeTable.getSelectedRow();
        if (selectedRow >= 0) {
            int roomTypeId = (int) roomTypeTable.getValueAt(selectedRow, 0);

            // Check if room type is associated with any room
            ArrayList<Room> rooms = RoomController.getInstance().getRoomList();
            for (Room room : rooms) {
                if (room.getRoomType().getRoomTypeId() == roomTypeId) {
                    JOptionPane.showMessageDialog(this, "Cannot edit room type associated with a room.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            RoomType roomType = RoomTypeController.getInstance().searchRoomTypeById(roomTypeId);

            int numberOfBeds = roomType.getNumberOfBeds();
            RoomCategory[] categories = getCategoriesByBeds(numberOfBeds);
            JComboBox<RoomCategory> categoryComboBox = new JComboBox<>(categories);
            categoryComboBox.setSelectedItem(roomType.getCategory());

            Object[] message = {
                "Number of Beds: " + numberOfBeds,
                "Category:", categoryComboBox
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Room Type", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String category = ((RoomCategory) categoryComboBox.getSelectedItem()).name();

                    // Check if the new room type data already exists
                    ArrayList<RoomType> roomTypes = RoomTypeController.getInstance().getRoomTypeList();
                    for (RoomType rt : roomTypes) {
                        if (rt.getRoomTypeId() != roomTypeId && rt.getNumberOfBeds() == numberOfBeds && rt.getCategory().name().equalsIgnoreCase(category)) {
                            JOptionPane.showMessageDialog(this, "Room type already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    RoomTypeController.getInstance().modifyRoomTypeCategory(roomTypeId, RoomCategory.valueOf(category));
                    updateRoomTypeTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No room type selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getMaxRoomTypeId() {
        ArrayList<RoomType> roomTypes = RoomTypeController.getInstance().getRoomTypeList();
        int maxId = 0;
        for (RoomType roomType : roomTypes) {
            if (roomType.getRoomTypeId() > maxId) {
                maxId = roomType.getRoomTypeId();
            }
        }
        return maxId;
    }
}
