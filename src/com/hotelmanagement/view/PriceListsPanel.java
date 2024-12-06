package com.hotelmanagement.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;

import com.hotelmanagement.controller.PriceListController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.model.PriceList;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.RoomType;
import com.hotelmanagement.model.AdditionalServices;

public class PriceListsPanel extends JPanel {

    private JTable priceListTable;
    private DefaultTableModel model;

    public PriceListsPanel() {
        setLayout(new BorderLayout());

        // Panel for Price Lists
        JPanel priceListPanel = new JPanel(new BorderLayout());
        JLabel priceListLabel = new JLabel("Price Lists");
        priceListLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceListPanel.add(priceListLabel, BorderLayout.NORTH);

        priceListTable = new JTable();
        updatePriceListTable();
        JScrollPane priceListScrollPane = new JScrollPane(priceListTable);
        priceListPanel.add(priceListScrollPane, BorderLayout.CENTER);

        // Adding buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add New Price List");
        JButton deleteButton = new JButton("Delete Price List");
        JButton editButton = new JButton("Edit Price List");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        add(priceListPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Adding action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewPriceList();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePriceList();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editPriceList();
            }
        });
    }

    private void updatePriceListTable() {
        String[] columnNames = {
            "ID", "Valid From", "Valid To", "Room Type Prices", "Additional Service Prices"
        };
        ArrayList<PriceList> priceLists = PriceListController.getInstance().getAllPriceList();
        model = new DefaultTableModel(columnNames, 0);

        for (PriceList priceList : priceLists) {
            Object[] row = {
                priceList.getPriceListId(),
                priceList.getValidFrom(),
                priceList.getValidTo(),
                mapRoomTypePricesToString(priceList.getRoomTypePrices()),
                mapAdditionalServicePricesToString(priceList.getAdditionalServicePrices())
            };
            model.addRow(row);
        }

        priceListTable.setModel(model);
    }

    private String mapRoomTypePricesToString(Map<Integer, Double> map) {
        if (map == null || map.isEmpty()) {
            return "N/A";
        }
        return map.entrySet().stream()
                  .map(entry -> getRoomTypeNameById(entry.getKey()) + ": " + entry.getValue())
                  .collect(Collectors.joining(", "));
    }

    private String mapAdditionalServicePricesToString(Map<Integer, Double> map) {
        if (map == null || map.isEmpty()) {
            return "N/A";
        }
        return map.entrySet().stream()
                  .map(entry -> getAdditionalServiceNameById(entry.getKey()) + ": " + entry.getValue())
                  .collect(Collectors.joining(", "));
    }

    private String getRoomTypeNameById(int id) {
        RoomType roomType = RoomTypeController.getInstance().searchRoomTypeById(id);
        return roomType != null ? roomType.getCategory().toString() : "Unknown";
    }

    private String getAdditionalServiceNameById(int id) {
        AdditionalServices service = AdditionalServicesController.getInstance().getAdditionalServiceById(id);
        return service != null ? service.getServiceName() : "Unknown";
    }

    private void addNewPriceList() {
        JTextField validFromField = new JTextField();
        JTextField validToField = new JTextField();
        
        JPanel roomTypePricesPanel = new JPanel(new GridLayout(0, 2));
        ArrayList<RoomType> roomTypes = RoomTypeController.getInstance().getRoomTypeList();
        Map<Integer, JTextField> roomTypePriceFields = roomTypes.stream()
                .collect(Collectors.toMap(RoomType::getRoomTypeId, rt -> {
                    JLabel label = new JLabel(rt.getCategory().toString());
                    JTextField field = new JTextField();
                    roomTypePricesPanel.add(label);
                    roomTypePricesPanel.add(field);
                    return field;
                }));

        JPanel additionalServicePricesPanel = new JPanel(new GridLayout(0, 2));
        ArrayList<AdditionalServices> additionalServices = AdditionalServicesController.getInstance().getAdditionalServicesList();
        Map<Integer, JTextField> additionalServicePriceFields = additionalServices.stream()
                .collect(Collectors.toMap(AdditionalServices::getServiceId, as -> {
                    JLabel label = new JLabel(as.getServiceName());
                    JTextField field = new JTextField();
                    additionalServicePricesPanel.add(label);
                    additionalServicePricesPanel.add(field);
                    return field;
                }));

        Object[] message = {
            "Valid From (yyyy-mm-dd):", validFromField,
            "Valid To (yyyy-mm-dd):", validToField,
            "Room Type Prices:", roomTypePricesPanel,
            "Additional Service Prices:", additionalServicePricesPanel
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Price List", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                LocalDate validFrom = LocalDate.parse(validFromField.getText());
                LocalDate validTo = LocalDate.parse(validToField.getText());
                if (validTo.isBefore(validFrom)) {
                    JOptionPane.showMessageDialog(this, "Valid To date cannot be before Valid From date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (isOverlappingWithExistingPriceLists(validFrom, validTo)) {
                    JOptionPane.showMessageDialog(this, "Price list dates overlap with existing price lists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Map<Integer, Double> roomTypePrices = roomTypePriceFields.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                            double price = Double.parseDouble(entry.getValue().getText());
                            if (price < 1) throw new IllegalArgumentException("Prices must be greater than or equal to 1.");
                            return price;
                        }));

                Map<Integer, Double> additionalServicePrices = additionalServicePriceFields.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                            double price = Double.parseDouble(entry.getValue().getText());
                            if (price < 1) throw new IllegalArgumentException("Prices must be greater than or equal to 1.");
                            return price;
                        }));
                int newPriceListId = getMaxPriceListId() + 1;
                PriceList newPriceList = new PriceList(newPriceListId, validFrom, validTo, roomTypePrices, additionalServicePrices);
                PriceListController.getInstance().addPriceList(newPriceList);
                PriceListController.getInstance().savePriceListsToFile();
                updatePriceListTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deletePriceList() {
        int selectedRow = priceListTable.getSelectedRow();
        if (selectedRow >= 0) {
            int priceListId = (int) priceListTable.getValueAt(selectedRow, 0);
            PriceList priceList = PriceListController.getInstance().getPriceListById(priceListId);
            if (priceList != null) {
            	if (isPriceListInUse(priceList)) {
                    JOptionPane.showMessageDialog(this, "Cannot delete price list. There are reservations within this price list period.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                PriceListController.getInstance().removePriceList(priceList);
                PriceListController.getInstance().savePriceListsToFile();
                updatePriceListTable();
            } else {
                JOptionPane.showMessageDialog(this, "Price List not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No Price List selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editPriceList() {
        int selectedRow = priceListTable.getSelectedRow();
        if (selectedRow >= 0) {
            int priceListId = (int) priceListTable.getValueAt(selectedRow, 0);
            PriceList priceList = PriceListController.getInstance().getPriceListById(priceListId);

            JPanel roomTypePricesPanel = new JPanel(new GridLayout(0, 2));
            Map<Integer, JTextField> roomTypePriceFields = priceList.getRoomTypePrices().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                        JLabel label = new JLabel(getRoomTypeNameById(entry.getKey()));
                        JTextField field = new JTextField(entry.getValue().toString());
                        roomTypePricesPanel.add(label);
                        roomTypePricesPanel.add(field);
                        return field;
                    }));

            JPanel additionalServicePricesPanel = new JPanel(new GridLayout(0, 2));
            Map<Integer, JTextField> additionalServicePriceFields = priceList.getAdditionalServicePrices().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                        JLabel label = new JLabel(getAdditionalServiceNameById(entry.getKey()));
                        JTextField field = new JTextField(entry.getValue().toString());
                        additionalServicePricesPanel.add(label);
                        additionalServicePricesPanel.add(field);
                        return field;
                    }));

            Object[] message = {
                "Room Type Prices:", roomTypePricesPanel,
                "Additional Service Prices:", additionalServicePricesPanel
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Price List", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                	Map<Integer, Double> roomTypePrices = roomTypePriceFields.entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                                double price = Double.parseDouble(entry.getValue().getText());
                                if (price < 1) throw new IllegalArgumentException("Prices must be greater than or equal to 1.");
                                return price;
                            }));

                    Map<Integer, Double> additionalServicePrices = additionalServicePriceFields.entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                                double price = Double.parseDouble(entry.getValue().getText());
                                if (price < 1) throw new IllegalArgumentException("Prices must be greater than or equal to 1.");
                                return price;
                            }));
                    priceList.setRoomTypePrices(roomTypePrices);
                    priceList.setAdditionalServicePrices(additionalServicePrices);

                    PriceListController.getInstance().updatePriceList(priceList);
                    PriceListController.getInstance().savePriceListsToFile();
                    updatePriceListTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No Price List selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getMaxPriceListId() {
        int maxId = 0;
        ArrayList<PriceList> priceLists = PriceListController.getInstance().getAllPriceList();
        for (PriceList priceList : priceLists) {
            if (priceList.getPriceListId() > maxId) {
                maxId = priceList.getPriceListId();
            }
        }
        return maxId;
    }
    
    private boolean isPriceListInUse(PriceList priceList) {
        ArrayList<Reservation> reservations = ReservationController.getInstance().getAllReservations();
        LocalDate validFrom = priceList.getValidFrom();
        LocalDate validTo = priceList.getValidTo();

        for (Reservation reservation : reservations) {
            if (!(reservation.getCheckOutDate().isBefore(validFrom) || reservation.getCheckInDate().isAfter(validTo))) {
                return true;
            }
        }
        return false;
    }

    private boolean isOverlappingWithExistingPriceLists(LocalDate validFrom, LocalDate validTo) {
        ArrayList<PriceList> priceLists = PriceListController.getInstance().getAllPriceList();
        for (PriceList priceList : priceLists) {
            if (!(validTo.isBefore(priceList.getValidFrom()) || validFrom.isAfter(priceList.getValidTo()))) {
                return true;
            }
        }
        return false;
    }
}
