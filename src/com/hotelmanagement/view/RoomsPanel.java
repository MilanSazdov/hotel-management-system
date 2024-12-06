package com.hotelmanagement.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.controller.RoomFeatureController;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomFeature;
import com.hotelmanagement.model.RoomStatus;
import com.hotelmanagement.model.RoomType;

public class RoomsPanel extends JPanel {

    private JTable roomTable;
    private DefaultTableModel model;

    public RoomsPanel() {
        setLayout(new BorderLayout());

        // Panel for Rooms
        JPanel roomPanel = new JPanel(new BorderLayout());
        JLabel roomLabel = new JLabel("Rooms");
        roomLabel.setHorizontalAlignment(SwingConstants.CENTER);
        roomPanel.add(roomLabel, BorderLayout.NORTH);

        roomTable = new JTable();
        updateRoomTable();
        JScrollPane roomScrollPane = new JScrollPane(roomTable);
        roomPanel.add(roomScrollPane, BorderLayout.CENTER);

        // Adding buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add New Room");
        JButton deleteButton = new JButton("Delete Room");
        JButton editButton = new JButton("Edit Room");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        add(roomPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Adding action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewRoom();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRoom();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editRoom();
            }
        });
    }

    private void updateRoomTable() {
        String[] columnNames = {
            "ID", "Room Number", "Room Type", "Status", "Description", "Check-In Dates", "Check-Out Dates"
        };
        ArrayList<Room> rooms = RoomController.getInstance().getAllRooms();
        model = new DefaultTableModel(columnNames, 0);

        for (Room room : rooms) {
            Object[] row = {
                room.getRoomId(),
                room.getRoomNumber(),
                room.getRoomType().getCategory(),
                room.getStatus(),
                room.getRoomDescription(),
                formatDates(room.getCheckInDates()),
                formatDates(room.getCheckOutDates())
            };
            model.addRow(row);
        }

        roomTable.setModel(model);
    }

    private void addNewRoom() {
        JTextField roomNumberField = new JTextField();
        JComboBox<RoomType> roomTypeComboBox = new JComboBox<>(RoomTypeController.getInstance().getRoomTypeList().toArray(new RoomType[0]));
        JTextField roomDescriptionField = new JTextField();
        JPanel featurePanel = new JPanel();
        featurePanel.setLayout(new BoxLayout(featurePanel, BoxLayout.Y_AXIS));
        EnumSet<RoomFeature> selectedFeatures = EnumSet.noneOf(RoomFeature.class);
        
        for (RoomFeature feature : RoomFeature.values()) {
            JCheckBox checkBox = new JCheckBox(feature.name().replace("_", " ").toLowerCase());
            checkBox.putClientProperty("feature", feature);
            featurePanel.add(checkBox);
        }

        Object[] message = {
            "Room Number:", roomNumberField,
            "Room Type:", roomTypeComboBox,
            "Description:", roomDescriptionField,
            "Features:", featurePanel
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Room", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int roomNumber = Integer.parseInt(roomNumberField.getText());
                RoomType roomType = (RoomType) roomTypeComboBox.getSelectedItem();
                String roomDescription = roomDescriptionField.getText();

                // Check if the room number already exists
                if (isRoomNumberExists(roomNumber)) {
                    JOptionPane.showMessageDialog(this, "Room number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int newRoomId = getMaxRoomId() + 1;
                Room newRoom = new Room(newRoomId, roomNumber, roomType, RoomStatus.FREE, roomDescription, new ArrayList<>(), new ArrayList<>());
                RoomController.getInstance().addRoom(newRoom);
                RoomController.getInstance().saveRoomsToFile();

                // Save selected features
                for (Component component : featurePanel.getComponents()) {
                    if (component instanceof JCheckBox) {
                        JCheckBox checkBox = (JCheckBox) component;
                        if (checkBox.isSelected()) {
                            selectedFeatures.add((RoomFeature) checkBox.getClientProperty("feature"));
                        }
                    }
                }

                // Only save features if any feature is selected
                if (!selectedFeatures.isEmpty()) {
                    RoomFeatureController.getInstance().saveRoomFeatures(newRoomId, newRoom.getRoomType().getRoomTypeId(), selectedFeatures);
                }

                updateRoomTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow >= 0) {
            int roomId = (int) roomTable.getValueAt(selectedRow, 0);
            Room room = RoomController.getInstance().findRoomById(roomId);
            if (room != null) {
                if (room.getStatus() != RoomStatus.FREE || !room.getCheckInDates().isEmpty() || !room.getCheckOutDates().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Cannot delete a room that is not FREE or has check-in/check-out dates.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                RoomController.getInstance().removeRoom(room);
                RoomController.getInstance().saveRoomsToFile();
                updateRoomTable();
            } else {
                JOptionPane.showMessageDialog(this, "Room not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No room selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow >= 0) {
            int roomId = (int) roomTable.getValueAt(selectedRow, 0);
            Room room = RoomController.getInstance().findRoomById(roomId);

            JTextField roomNumberField = new JTextField(String.valueOf(room.getRoomNumber()));
            JTextField roomDescriptionField = new JTextField(room.getRoomDescription());
            JPanel featurePanel = new JPanel();
            featurePanel.setLayout(new BoxLayout(featurePanel, BoxLayout.Y_AXIS));
            Set<RoomFeature> currentFeatures = RoomFeatureController.getInstance().getFeaturesForRoom(room.getRoomType().getRoomTypeId(), roomId);
            EnumSet<RoomFeature> updatedFeatures = EnumSet.noneOf(RoomFeature.class);
            
            for (RoomFeature feature : RoomFeature.values()) {
                JCheckBox checkBox = new JCheckBox(feature.name().replace("_", " ").toLowerCase());
                checkBox.putClientProperty("feature", feature);
                checkBox.setSelected(currentFeatures.contains(feature));
                featurePanel.add(checkBox);
            }

            Object[] message = {
                "Room Number:", roomNumberField,
                "Description:", roomDescriptionField,
                "Features:", featurePanel
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Room", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int roomNumber = Integer.parseInt(roomNumberField.getText());
                    String roomDescription = roomDescriptionField.getText();

                    // Check if the room number already exists for other rooms
                    if (roomNumber != room.getRoomNumber() && isRoomNumberExists(roomNumber)) {
                        JOptionPane.showMessageDialog(this, "Room number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    room.setRoomNumber(roomNumber);
                    room.setRoomDescription(roomDescription);

                    RoomController.getInstance().updateRoomAttributes(room);
                    RoomController.getInstance().saveRoomsToFile();

                    // Update selected features
                    for (Component component : featurePanel.getComponents()) {
                        if (component instanceof JCheckBox) {
                            JCheckBox checkBox = (JCheckBox) component;
                            if (checkBox.isSelected()) {
                                updatedFeatures.add((RoomFeature) checkBox.getClientProperty("feature"));
                            }
                        }
                    }

                    // Only save features if any feature is selected
                    if (!updatedFeatures.isEmpty()) {
                        RoomFeatureController.getInstance().saveRoomFeatures(roomId, room.getRoomType().getRoomTypeId(), updatedFeatures);
                    } else {
                        RoomFeatureController.getInstance().removeRoomFeatures(roomId);
                    }

                    updateRoomTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No room selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getMaxRoomId() {
        int maxId = 0;
        ArrayList<Room> rooms = RoomController.getInstance().getAllRooms();
        for (Room room : rooms) {
            if (room.getRoomId() > maxId) {
                maxId = room.getRoomId();
            }
        }
        return maxId;
    }

    private boolean isRoomNumberExists(int roomNumber) {
        ArrayList<Room> rooms = RoomController.getInstance().getAllRooms();
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return true;
            }
        }
        return false;
    }

    private String formatDates(ArrayList<LocalDate> dates) {
        StringBuilder sb = new StringBuilder();
        for (LocalDate date : dates) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(date.toString());
        }
        return sb.toString();
    }
}
