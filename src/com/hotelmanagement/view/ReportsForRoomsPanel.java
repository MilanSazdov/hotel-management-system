package com.hotelmanagement.view;

import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.model.PriceList;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomType;
import com.hotelmanagement.controller.PriceListController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ReportsForRoomsPanel extends JPanel {

    private JTextField startDateField;
    private JTextField endDateField;
    private JTable roomsTable;
    private DefaultTableModel model;
    private JLabel noRoomsLabel;
    private JScrollPane scrollPane;

    public ReportsForRoomsPanel() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());

        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        startDateField = new JTextField(10);
        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        endDateField = new JTextField(10);

        JButton searchButton = new JButton("Search");

        inputPanel.add(startDateLabel);
        inputPanel.add(startDateField);
        inputPanel.add(endDateLabel);
        inputPanel.add(endDateField);
        inputPanel.add(searchButton);

        add(inputPanel, BorderLayout.NORTH);

        roomsTable = new JTable();
        roomsTable.setFillsViewportHeight(true);
        roomsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        roomsTable.setRowHeight(30);
        scrollPane = new JScrollPane(roomsTable);
        add(scrollPane, BorderLayout.CENTER);

        noRoomsLabel = new JLabel("No room data found for the selected period.", SwingConstants.CENTER);
        noRoomsLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchRoomData();
            }
        });
    }

    private void searchRoomData() {
        try {
            LocalDate startDate = LocalDate.parse(startDateField.getText());
            LocalDate endDate = LocalDate.parse(endDateField.getText());
            if (startDate.isAfter(endDate)) {
                JOptionPane.showMessageDialog(this, "Start date must be before end date.");
                return;
            }

            String[] columnNames = {"Room ID", "Room Type", "Total Nights", "Income"};
            model = new DefaultTableModel(columnNames, 0);

            RoomController roomController = RoomController.getInstance();
            ReservationController reservationController = ReservationController.getInstance();
            RoomTypeController roomTypeController = RoomTypeController.getInstance();

            List<RoomData> roomDataList = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader("src/com/hotelmanagement/data/confirmed_reservations_by_period.csv"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    int reservationId = Integer.parseInt(values[1]);

                    Reservation reservation = reservationController.findReservationById(reservationId);
                    if (reservation != null && reservationFallsWithinPeriod(reservation, startDate, endDate)) {
                        int roomId = reservation.getRoomId();
                        Room room = roomController.findRoomById(roomId);

                        if (room == null) {
                            System.out.println("Room with ID " + roomId + " not found for reservation ID " + reservationId);
                            continue;
                        }

                        RoomType roomType = roomTypeController.searchRoomTypeById(room.getRoomType().getRoomTypeId());
                        if (roomType == null) {
                            System.out.println("RoomType for room ID " + roomId + " not found.");
                            continue;
                        }

                        long totalNights = calculateNightsInRange(reservation, startDate, endDate);
                        double roomIncome = calculateRoomIncome(reservation, startDate, endDate);

                        RoomData roomData = findRoomData(roomDataList, roomId);
                        if (roomData == null) {
                            roomData = new RoomData(roomId, roomType, totalNights, roomIncome);
                            roomDataList.add(roomData);
                        } else {
                            roomData.addNights(totalNights);
                            roomData.addIncome(roomIncome);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (RoomData roomData : roomDataList) {
                Object[] row = {
                        roomData.getRoomId(),
                        roomData.getRoomType().getCategory().name(),
                        roomData.getTotalNights(),
                        roomData.getIncome()
                };
                model.addRow(row);
            }

            if (roomDataList.isEmpty()) {
                roomsTable.setModel(new DefaultTableModel());
                remove(scrollPane);
                add(noRoomsLabel, BorderLayout.CENTER);
            } else {
                remove(noRoomsLabel);
                roomsTable.setModel(model);
                add(scrollPane, BorderLayout.CENTER);
            }

            revalidate();
            repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error parsing dates or calculating data: " + ex.getMessage());
        }
    }


    private boolean reservationFallsWithinPeriod(Reservation reservation, LocalDate startDate, LocalDate endDate) {
        return !(reservation.getCheckOutDate().isBefore(startDate) || reservation.getCheckInDate().isAfter(endDate));
    }

    private long calculateNightsInRange(Reservation reservation, LocalDate startDate, LocalDate endDate) {
        LocalDate checkInDate = reservation.getCheckInDate().isBefore(startDate) ? startDate : reservation.getCheckInDate();
        LocalDate checkOutDate = reservation.getCheckOutDate().isAfter(endDate) ? endDate : reservation.getCheckOutDate();
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate.plusDays(1)); // Including the checkout day
    }

    private double calculateRoomIncome(Reservation reservation, LocalDate startDate, LocalDate endDate) {
        double totalIncome = 0.0;
        LocalDate tempDate = startDate.isBefore(reservation.getCheckInDate()) ? reservation.getCheckInDate() : startDate;
        LocalDate finalDate = endDate.isAfter(reservation.getCheckOutDate()) ? reservation.getCheckOutDate() : endDate;

        while (!tempDate.isAfter(finalDate)) {
            totalIncome += getDailyRoomRate(tempDate, reservation.getRoomTypeId());
            tempDate = tempDate.plusDays(1);
        }

        return totalIncome;
    }

    private double getDailyRoomRate(LocalDate date, int roomTypeId) {
        PriceList applicablePriceList = PriceListController.getInstance().getApplicablePriceListForDate(date);
        if (applicablePriceList != null) {
            return applicablePriceList.getPriceForRoomTypeId(roomTypeId);
        }
        return 0.0;
    }

    private RoomData findRoomData(List<RoomData> roomDataList, int roomId) {
        for (RoomData roomData : roomDataList) {
            if (roomData.getRoomId() == roomId) {
                return roomData;
            }
        }
        return null;
    }

    private static class RoomData {
        private int roomId;
        private RoomType roomType;
        private long totalNights;
        private double income;

        public RoomData(int roomId, RoomType roomType, long totalNights, double income) {
            this.roomId = roomId;
            this.roomType = roomType;
            this.totalNights = totalNights;
            this.income = income;
        }

        public int getRoomId() {
            return roomId;
        }

        public RoomType getRoomType() {
            return roomType;
        }

        public long getTotalNights() {
            return totalNights;
        }

        public double getIncome() {
            return income;
        }

        public void addNights(long nights) {
            this.totalNights += nights;
        }

        public void addIncome(double income) {
            this.income += income;
        }
    }
}
