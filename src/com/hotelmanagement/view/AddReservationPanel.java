package com.hotelmanagement.view;

import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.model.AdditionalServices;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;
import com.hotelmanagement.model.RoomType;
import com.hotelmanagement.model.RoomFeature;
import com.hotelmanagement.model.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AddReservationPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField checkInField;
    private JTextField checkOutField;
    private JComboBox<String> roomTypeComboBox;
    private JPanel servicesPanel;
    private List<JCheckBox> serviceCheckBoxes;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Map<String, RoomType> roomTypeMap;
    private List<AdditionalServices> selectedServices = new ArrayList<>();
    private int guestId;
    private ReservationsPanel reservationsPanel;
    private JPanel featurePanel;
    private List<JCheckBox> featureCheckBoxes;

    public AddReservationPanel(int guestId, ReservationsPanel reservationsPanel) {
        this.guestId = guestId;
        this.reservationsPanel = reservationsPanel;
        setLayout(new BorderLayout());

        // Panel for entering dates and searching room types
        JPanel datePanel = new JPanel(new FlowLayout());

        JLabel checkInLabel = new JLabel("Check-In Date (YYYY-MM-DD):");
        checkInField = new JTextField(10);
        datePanel.add(checkInLabel);
        datePanel.add(checkInField);

        JLabel checkOutLabel = new JLabel("Check-Out Date (YYYY-MM-DD):");
        checkOutField = new JTextField(10);
        datePanel.add(checkOutLabel);
        datePanel.add(checkOutField);

        JButton searchButton = new JButton("Search Room Types");
        datePanel.add(searchButton);

        add(datePanel, BorderLayout.NORTH);

        // Panel for displaying available room types
        JPanel roomTypePanel = new JPanel(new FlowLayout());
        roomTypeComboBox = new JComboBox<>();
        roomTypePanel.add(new JLabel("Select Room Type:"));
        roomTypePanel.add(roomTypeComboBox);
        add(roomTypePanel, BorderLayout.CENTER);

        // Panel for displaying additional services
        servicesPanel = new JPanel();
        servicesPanel.setLayout(new BoxLayout(servicesPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(servicesPanel), BorderLayout.EAST);

        // Panel for displaying room features
        featurePanel = new JPanel();
        featurePanel.setLayout(new BoxLayout(featurePanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(featurePanel), BorderLayout.WEST);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Reservation");
        buttonPanel.add(addButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listener for search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String checkInStr = checkInField.getText();
                String checkOutStr = checkOutField.getText();
                try {
                    checkInDate = LocalDate.parse(checkInStr);
                    checkOutDate = LocalDate.parse(checkOutStr);
                    
                    // Check if check-in date is in the past
					if (checkInDate.isBefore(LocalDate.now())) {
						JOptionPane.showMessageDialog(AddReservationPanel.this, "Check-In date cannot be in the past.");
						return;
					}
                    
					// Check if check-out date is before check-in date
                    if (checkOutDate.isBefore(checkInDate)) {
                        JOptionPane.showMessageDialog(AddReservationPanel.this, "Check-Out date cannot be before Check-In date.");
                        return;
                    }
                    
                    updateRoomTypes(checkInDate, checkOutDate);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AddReservationPanel.this, "Invalid date format.");
                }
            }
        });

        // Action listener for add reservation button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (roomTypeComboBox.getSelectedItem() != null) {
                    String selectedRoomTypeName = (String) roomTypeComboBox.getSelectedItem();
                    RoomType selectedRoomType = roomTypeMap.get(selectedRoomTypeName);

                    if (selectedRoomType != null) {
                        ArrayList<Integer> additionalServiceIds = new ArrayList<>();
                        for (JCheckBox checkBox : serviceCheckBoxes) {
                            if (checkBox.isSelected()) {
                                int serviceId = (int) checkBox.getClientProperty("serviceId");
                                additionalServiceIds.add(serviceId);
                            }
                        }

                        int newReservationId = getNextReservationId();
                        Reservation reservation = new Reservation(newReservationId, guestId, -1, selectedRoomType.getRoomTypeId(), checkInDate, checkOutDate, ReservationStatus.WAITING, additionalServiceIds);

                        ReservationController.getInstance().addReservation(reservation);
                        reservationsPanel.updateReservationsTable(); // Update reservations table
                        JOptionPane.showMessageDialog(AddReservationPanel.this, "Reservation added successfully.");

                        // Write reservation details to CSV
                        writeReservationToCSV(reservation);
                    } else {
                        JOptionPane.showMessageDialog(AddReservationPanel.this, "Failed to retrieve the selected room type.");
                    }
                } else {
                    JOptionPane.showMessageDialog(AddReservationPanel.this, "Please select a room type.");
                }
            }
        });

        // Initialize additional services checkboxes
        updateServices();

        // Initialize room features checkboxes
        updateFeatures();
    }

    private void updateRoomTypes(LocalDate checkIn, LocalDate checkOut) {
        RoomController roomController = RoomController.getInstance();
        EnumSet<RoomFeature> requiredFeatures = EnumSet.noneOf(RoomFeature.class);

        for (JCheckBox checkBox : featureCheckBoxes) {
            if (checkBox.isSelected()) {
                requiredFeatures.add((RoomFeature) checkBox.getClientProperty("feature"));
            }
        }

        List<RoomType> roomTypes = RoomTypeController.getInstance().getRoomTypeList();
        roomTypeComboBox.removeAllItems();
        roomTypeMap = new HashMap<>();

        for (RoomType roomType : roomTypes) {
            List<Room> availableRooms = roomController.getFreeRoomsByTypeAndPeriod(roomType, checkIn, checkOut);

            for (Room room : availableRooms) {
                EnumSet<RoomFeature> roomFeatures = getRoomFeatures(room.getRoomId());
                if (requiredFeatures.isEmpty() || roomFeatures.containsAll(requiredFeatures)) {
                    String roomTypeName = roomType.getCategory().name();
                    roomTypeComboBox.addItem(roomTypeName);
                    roomTypeMap.put(roomTypeName, roomType);
                    break;
                }
            }
        }

        if (roomTypeComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No room types available with the selected features.");
        }
    }

    private EnumSet<RoomFeature> getRoomFeatures(int roomId) {
        EnumSet<RoomFeature> features = EnumSet.noneOf(RoomFeature.class);
        try (BufferedReader br = new BufferedReader(new FileReader("src/com/hotelmanagement/data/room_features.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[1]);
                if (id == roomId) {
                    for (int i = 2; i < parts.length; i++) {
                        features.add(RoomFeature.valueOf(parts[i]));
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return features;
    }

    private void updateServices() {
        List<AdditionalServices> services = AdditionalServicesController.getInstance().getAdditionalServicesList();
        servicesPanel.removeAll();
        serviceCheckBoxes = new ArrayList<>();
        for (AdditionalServices service : services) {
            JCheckBox checkBox = new JCheckBox(service.getServiceName());
            checkBox.putClientProperty("serviceId", service.getServiceId());
            servicesPanel.add(checkBox);
            serviceCheckBoxes.add(checkBox);
        }
        servicesPanel.revalidate();
        servicesPanel.repaint();
    }

    private void updateFeatures() {
        featurePanel.removeAll();
        featureCheckBoxes = new ArrayList<>();
        for (RoomFeature feature : RoomFeature.values()) {
            JCheckBox checkBox = new JCheckBox(feature.name().replace("_", " ").toLowerCase());
            checkBox.putClientProperty("feature", feature);
            featurePanel.add(checkBox);
            featureCheckBoxes.add(checkBox);
        }
        featurePanel.revalidate();
        featurePanel.repaint();
    }

    private int getNextReservationId() {
        int maxId = 0;
        List<Reservation> reservations = ReservationController.getInstance().getAllReservations();
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId() > maxId) {
                maxId = reservation.getReservationId();
            }
        }
        return maxId + 1;
    }

    private void writeReservationToCSV(Reservation reservation) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("src/com/hotelmanagement/data/created_reservations_by_period.csv", true))) {
            writer.printf("%d,%s,%s%n", reservation.getReservationId(), reservation.getStatus(), LocalDate.now());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to CSV file.");
        }
    }
}
