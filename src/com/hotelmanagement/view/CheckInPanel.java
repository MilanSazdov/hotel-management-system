package com.hotelmanagement.view;

import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.controller.GuestController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.model.AdditionalServices;
import com.hotelmanagement.model.Gender;
import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomStatus;
import com.hotelmanagement.model.RoomType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckInPanel extends JPanel {

    private JTextField usernameField;
    private JTable reservationsTable;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JLabel noReservationsLabel;
    private Guest currentGuest;
    private int receptionistId;

    public CheckInPanel(int receptionistId) {
        this.receptionistId = receptionistId;
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel usernameLabel = new JLabel("Enter Guest Username:");
        usernameField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(searchButton);

        add(inputPanel, BorderLayout.NORTH);

        reservationsTable = new JTable();
        reservationsTable.setFillsViewportHeight(true);
        reservationsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        reservationsTable.setRowHeight(30);
        scrollPane = new JScrollPane(reservationsTable);
        add(scrollPane, BorderLayout.CENTER);

        noReservationsLabel = new JLabel("No reservations found for the current date.", SwingConstants.CENTER);
        noReservationsLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton checkInButton = new JButton("Check In");

        buttonPanel.add(checkInButton);

        add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                currentGuest = GuestController.getInstance().getGuestByEmail(username);
                if (currentGuest != null) {
                    updateReservationsTable(currentGuest.getGuestId());
                } else {
                    int response = JOptionPane.showConfirmDialog(CheckInPanel.this, "Guest not found. Do you want to create a new account?", "Guest Not Found", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        createNewGuestAccount(username);
                    }
                }
            }
        });

        checkInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = reservationsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int reservationId = (int) reservationsTable.getValueAt(selectedRow, 0);
                    checkInReservation(reservationId);
                } else {
                    JOptionPane.showMessageDialog(CheckInPanel.this, "Please select a reservation to check in.");
                }
            }
        });
    }

    private void createNewGuestAccount(String email) {
        JTextField nameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JComboBox<Gender> genderComboBox = new JComboBox<>(Gender.values());
        JTextField birthDateField = new JTextField();
        JTextField phoneNumberField = new JTextField();
        JTextField passportNumberField = new JTextField();

        Object[] message = {
            "First Name:", nameField,
            "Last Name:", lastNameField,
            "Gender:", genderComboBox,
            "Birth Date (YYYY-MM-DD):", birthDateField,
            "Phone Number:", phoneNumberField,
            "Email:", new JLabel(email),
            "Passport Number:", passportNumberField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Register New Guest", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                Gender gender = (Gender) genderComboBox.getSelectedItem();
                LocalDate birthDate = LocalDate.parse(birthDateField.getText().trim());
                String phoneNumber = phoneNumberField.getText().trim();
                String passportNumber = passportNumberField.getText().trim();

                if (name.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || passportNumber.isEmpty() || birthDate.isAfter(LocalDate.now().minusYears(10))) {
                    throw new IllegalArgumentException("All fields must be filled and birthdate must be older than 10 years.");
                }

                if (!phoneNumber.matches("\\d+")) {
                    throw new IllegalArgumentException("Phone number must contain only digits.");
                }

                Guest newGuest = new Guest(getNextGuestId(), name, lastName, gender, birthDate, phoneNumber, email, passportNumber, new ArrayList<>());
                GuestController.getInstance().addGuest(newGuest);
                GuestController.getInstance().appendGuestToFile(newGuest);

                currentGuest = newGuest;

                JOptionPane.showMessageDialog(this, "Guest registered successfully.");
                createNewReservationForGuest(newGuest.getGuestId());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid birthdate format.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createNewReservationForGuest(int guestId) {
        JTextField checkOutField = new JTextField();
        JButton searchRoomsButton = new JButton("Search Available Rooms");

        Object[] dateMessage = {
            "Check-Out Date (YYYY-MM-DD):", checkOutField,
            searchRoomsButton
        };

        int dateOption = JOptionPane.showConfirmDialog(this, dateMessage, "Enter Dates", JOptionPane.OK_CANCEL_OPTION);
        if (dateOption == JOptionPane.OK_OPTION) {
            try {
                LocalDate checkInDate = LocalDate.now();
                LocalDate checkOutDate = LocalDate.parse(checkOutField.getText().trim());

                if (checkOutDate.isBefore(checkInDate)) {
                    throw new IllegalArgumentException("Check-Out date cannot be before Check-In date.");
                }

                searchRoomsButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        List<RoomType> roomTypes = RoomTypeController.getInstance().getRoomTypeList();
                        List<Room> availableRooms = new ArrayList<>();
                        for (RoomType roomType : roomTypes) {
                            availableRooms.addAll(RoomController.getInstance().getFreeRoomsByTypeAndPeriod(roomType, checkInDate, checkOutDate));
                        }

                        if (!availableRooms.isEmpty()) {
                            JComboBox<String> roomComboBox = new JComboBox<>();
                            for (Room room : availableRooms) {
                                RoomType roomType = RoomTypeController.getInstance().searchRoomTypeById(room.getRoomType().getRoomTypeId());
                                roomComboBox.addItem("Room Number: " + room.getRoomNumber() + " - Type: " + roomType.getCategory().name());
                            }

                            Object[] roomMessage = {
                                "Select Room:", roomComboBox
                            };

                            int roomOption = JOptionPane.showConfirmDialog(CheckInPanel.this, roomMessage, "Available Rooms", JOptionPane.OK_CANCEL_OPTION);
                            if (roomOption == JOptionPane.OK_OPTION) {
                                Room selectedRoom = availableRooms.get(roomComboBox.getSelectedIndex());
                                int newReservationId = getNextReservationId();
                                Reservation newReservation = new Reservation(newReservationId, guestId, selectedRoom.getRoomId(), selectedRoom.getRoomType().getRoomTypeId(), checkInDate, checkOutDate, ReservationStatus.CHECKED_IN, new ArrayList<>());
                                ReservationController.getInstance().addReservation(newReservation);
                                ReservationController.getInstance().saveReservationsToFile();

                                selectedRoom.setStatus(RoomStatus.OCCUPIED);
                                selectedRoom.getCheckInDates().add(checkInDate);
                                selectedRoom.getCheckOutDates().add(checkOutDate);
                                RoomController.getInstance().updateRoomAttributes(selectedRoom);
                                RoomController.getInstance().saveRoomsToFile();

                                writeCheckedInReservationToFile(newReservationId);

                                JOptionPane.showMessageDialog(CheckInPanel.this, "Reservation created and checked in successfully. You have been assigned to Room Number " + selectedRoom.getRoomNumber());
                            }
                        } else {
                            JOptionPane.showMessageDialog(CheckInPanel.this, "No available rooms for the selected type and period.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                searchRoomsButton.doClick();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateReservationsTable(int guestId) {
        String[] columnNames = {"Reservation ID", "Room Type", "Check-In Date", "Check-Out Date", "Total Cost"};
        model = new DefaultTableModel(columnNames, 0);

        ReservationController reservationController = ReservationController.getInstance();
        LocalDate today = LocalDate.now();
        List<Reservation> reservations = reservationController.getReservationsByGuestId(guestId)
                .stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.CONFIRMED
                        && !today.isBefore(reservation.getCheckInDate())
                        && !today.isAfter(reservation.getCheckOutDate()))
                .collect(Collectors.toList());

        if (reservations.isEmpty()) {
            reservationsTable.setModel(new DefaultTableModel());
            remove(scrollPane);
            add(noReservationsLabel, BorderLayout.CENTER);
        } else {
            remove(noReservationsLabel);
            for (Reservation reservation : reservations) {
                Object[] row = {
                    reservation.getReservationId(),
                    RoomTypeController.getInstance().searchRoomTypeById(reservation.getRoomTypeId()).getCategory().name(),
                    reservation.getCheckInDate(),
                    reservation.getCheckOutDate(),
                    reservation.getTotalCost()
                };
                model.addRow(row);
            }
            reservationsTable.setModel(model);
            add(scrollPane, BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }

    private void checkInReservation(int reservationId) {
        ReservationController reservationController = ReservationController.getInstance();
        RoomController roomController = RoomController.getInstance();

        Reservation reservation = reservationController.findReservationById(reservationId);
        if (reservation != null && reservation.getStatus() == ReservationStatus.CONFIRMED) {
            Room room = roomController.findRoomById(reservation.getRoomId());
            if (room != null) {
                room.setStatus(RoomStatus.OCCUPIED);
                room.getCheckInDates().add(reservation.getCheckInDate());
                room.getCheckOutDates().add(reservation.getCheckOutDate());
                roomController.updateRoomAttributes(room);
                roomController.saveRoomsToFile(); // Ensure the room status is saved
            }

            List<AdditionalServices> additionalServices = getAvailableAdditionalServices(reservation);
            if (!additionalServices.isEmpty()) {
                showAdditionalServicesDialog(reservation, additionalServices);
            }

            reservation.setStatus(ReservationStatus.CHECKED_IN);
            reservationController.updateReservation(reservation);
            reservationController.saveReservationsToFile();

            writeCheckedInReservationToFile(reservationId);

            updateReservationsTable(currentGuest.getGuestId());
            JOptionPane.showMessageDialog(this, "Check-in successful. You have been assigned to Room Number " + room.getRoomNumber());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to check in the reservation.");
        }
    }

    private List<AdditionalServices> getAvailableAdditionalServices(Reservation reservation) {
        List<AdditionalServices> allAdditionalServices = AdditionalServicesController.getInstance().getAdditionalServicesList();
        List<AdditionalServices> alreadyAddedServices = reservation.getAdditionalServiceIds().stream()
                .map(id -> AdditionalServicesController.getInstance().getAdditionalServiceById(id))
                .collect(Collectors.toList());

        return allAdditionalServices.stream()
                .filter(service -> !alreadyAddedServices.contains(service))
                .collect(Collectors.toList());
    }

    private void showAdditionalServicesDialog(Reservation reservation, List<AdditionalServices> additionalServices) {
        JDialog dialog = new JDialog((Frame) null, "Select Additional Services", true);
        dialog.setLayout(new BorderLayout());

        JPanel servicesPanel = new JPanel();
        servicesPanel.setLayout(new BoxLayout(servicesPanel, BoxLayout.Y_AXIS));
        List<JCheckBox> additionalServiceCheckBoxes = new ArrayList<>();

        for (AdditionalServices service : additionalServices) {
            JCheckBox checkBox = new JCheckBox(service.getServiceName());
            checkBox.putClientProperty("serviceId", service.getServiceId());
            servicesPanel.add(checkBox);
            additionalServiceCheckBoxes.add(checkBox);
        }

        dialog.add(new JScrollPane(servicesPanel), BorderLayout.CENTER);

        JButton addButton = new JButton("Add Services");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox checkBox : additionalServiceCheckBoxes) {
                    if (checkBox.isSelected()) {
                        int serviceId = (int) checkBox.getClientProperty("serviceId");
                        reservation.getAdditionalServiceIds().add(serviceId);
                    }
                }
                reservation.calculateTotalCost();
                ReservationController.getInstance().updateReservation(reservation);
                ReservationController.getInstance().saveReservationsToFile();
                dialog.dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void writeCheckedInReservationToFile(int reservationId) {
        String path = "src/com/hotelmanagement/data/checked_in_reservations_by_period.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(reservationId + "," + receptionistId + "," + LocalDate.now() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getNextGuestId() {
        int maxId = 0;
        ArrayList<Guest> guests = GuestController.getInstance().getAllGuests();
        for (Guest guest : guests) {
            if (guest.getGuestId() > maxId) {
                maxId = guest.getGuestId();
            }
        }
        return maxId + 1;
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
}
