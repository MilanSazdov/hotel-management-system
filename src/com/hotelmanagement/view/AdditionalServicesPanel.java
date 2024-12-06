package com.hotelmanagement.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.controller.PriceListController;
import com.hotelmanagement.model.AdditionalServices;
import com.hotelmanagement.model.PriceList;

public class AdditionalServicesPanel extends JPanel {

    private JTable additionalServicesTable;
    private DefaultTableModel model;

    public AdditionalServicesPanel() {
        setLayout(new BorderLayout());

        // Panel for Additional Services
        JPanel additionalServicesPanel = new JPanel(new BorderLayout());
        JLabel additionalServicesLabel = new JLabel("Additional Services");
        additionalServicesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        additionalServicesPanel.add(additionalServicesLabel, BorderLayout.NORTH);

        additionalServicesTable = new JTable();
        updateAdditionalServicesTable();
        JScrollPane additionalServicesScrollPane = new JScrollPane(additionalServicesTable);
        additionalServicesPanel.add(additionalServicesScrollPane, BorderLayout.CENTER);

        // Adding buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Service");
        JButton deleteButton = new JButton("Delete Service");
        JButton editButton = new JButton("Edit Service");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        add(additionalServicesPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Adding action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewService();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteService();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editService();
            }
        });
    }

    private void updateAdditionalServicesTable() {
        String[] columnNames = {
            "ID", "Service Name"
        };
        ArrayList<AdditionalServices> additionalServices = AdditionalServicesController.getInstance().getAdditionalServicesList();
        model = new DefaultTableModel(columnNames, 0);

        for (AdditionalServices additionalService : additionalServices) {
            Object[] row = {
                additionalService.getServiceId(),
                additionalService.getServiceName()
            };
            model.addRow(row);
        }

        additionalServicesTable.setModel(model);
    }

    private void addNewService() {
        JTextField serviceNameField = new JTextField();

        Object[] message = {
            "Service Name:", serviceNameField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Service", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String serviceName = serviceNameField.getText().trim();

            if (serviceName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Service name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!isServiceNameValid(serviceName)) {
                JOptionPane.showMessageDialog(this, "Service name cannot contain numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (isServiceNameExists(serviceName)) {
                JOptionPane.showMessageDialog(this, "Service name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int newServiceId = getMaxServiceId() + 1;
            AdditionalServices newService = new AdditionalServices(newServiceId, serviceName);
            AdditionalServicesController.getInstance().addAdditionalService(newService);
            updateAdditionalServicesTable();
        }
    }

    private void deleteService() {
        int selectedRow = additionalServicesTable.getSelectedRow();
        if (selectedRow >= 0) {
            int serviceId = (int) additionalServicesTable.getValueAt(selectedRow, 0);
            AdditionalServices service = AdditionalServicesController.getInstance().getAdditionalServiceById(serviceId);
            if (service != null) {
            	if (isServiceInPriceList(serviceId)) {
                    JOptionPane.showMessageDialog(this, "Cannot delete service. It is included in at least one price list.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                AdditionalServicesController.getInstance().removeAdditionalService(service);
                updateAdditionalServicesTable();
            } else {
                JOptionPane.showMessageDialog(this, "Service not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No service selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editService() {
        int selectedRow = additionalServicesTable.getSelectedRow();
        if (selectedRow >= 0) {
            int serviceId = (int) additionalServicesTable.getValueAt(selectedRow, 0);
            AdditionalServices service = AdditionalServicesController.getInstance().getAdditionalServiceById(serviceId);
            if (service != null) {
                JTextField serviceNameField = new JTextField(service.getServiceName());

                Object[] message = {
                    "Service Name:", serviceNameField
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Edit Service", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String serviceName = serviceNameField.getText().trim();

                    if (serviceName.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Service name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (!isServiceNameValid(serviceName)) {
                        JOptionPane.showMessageDialog(this, "Service name cannot contain numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!service.getServiceName().equals(serviceName) && isServiceNameExists(serviceName)) {
                        JOptionPane.showMessageDialog(this, "Service name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    service.setServiceName(serviceName);
                    AdditionalServicesController.getInstance().updateAdditionalService(service);
                    updateAdditionalServicesTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Service not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No service selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isServiceNameExists(String serviceName) {
        ArrayList<AdditionalServices> services = AdditionalServicesController.getInstance().getAdditionalServicesList();
        for (AdditionalServices service : services) {
            if (service.getServiceName().equalsIgnoreCase(serviceName)) {
                return true;
            }
        }
        return false;
    }

    private int getMaxServiceId() {
        ArrayList<AdditionalServices> services = AdditionalServicesController.getInstance().getAdditionalServicesList();
        int maxId = 0;
        for (AdditionalServices service : services) {
            if (service.getServiceId() > maxId) {
                maxId = service.getServiceId();
            }
        }
        return maxId;
    }
    
    private boolean isServiceNameValid(String serviceName) {
        return !Pattern.compile("[0-9]").matcher(serviceName).find();
    }

    private boolean isServiceInPriceList(int serviceId) {
        ArrayList<PriceList> priceLists = PriceListController.getInstance().getAllPriceList();
        for (PriceList priceList : priceLists) {
            if (priceList.getAdditionalServiceIds().contains(serviceId)) {
                return true;
            }
        }
        return false;
    }
}
