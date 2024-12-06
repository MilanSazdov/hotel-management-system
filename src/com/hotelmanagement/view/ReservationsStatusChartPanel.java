package com.hotelmanagement.view;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.PieStyler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ReservationsStatusChartPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final String[] FILE_PATHS = {
            "src/com/hotelmanagement/data/checked_out_reservations_by_period.csv",
            "src/com/hotelmanagement/data/checked_in_reservations_by_period.csv",
            "src/com/hotelmanagement/data/changed_mind_reservations_by_period.csv",
            "src/com/hotelmanagement/data/rejected_reservations_by_period.csv",
            "src/com/hotelmanagement/data/canceled_reservations_by_period.csv",
            "src/com/hotelmanagement/data/confirmed_reservations_by_period.csv",
            "src/com/hotelmanagement/data/created_reservations_by_period.csv"
    };
    private static final String[] STATUSES = {
            "CHECKED_OUT",
            "CHECKED_IN",
            "CHANGED_MIND",
            "REJECTED",
            "CANCELLED",
            "CONFIRMED",
            "WAITING"
    };

    public ReservationsStatusChartPanel() {
        setLayout(new BorderLayout());

        // Load data
        Map<Integer, String> reservationStatusMap = new HashMap<>();
        Map<Integer, LocalDate> reservationDateMap = new HashMap<>();
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Status", "Date"}, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < FILE_PATHS.length; i++) {
            String filePath = FILE_PATHS[i];
            String status = STATUSES[i];
            boolean isCreatedReservationsFile = filePath.equals("src/com/hotelmanagement/data/created_reservations_by_period.csv");

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    int reservationId = isCreatedReservationsFile ? Integer.parseInt(parts[0]) : Integer.parseInt(parts[1]);
                    String actualStatus = isCreatedReservationsFile ? "WAITING" : status;
                    LocalDate date = LocalDate.parse(parts[isCreatedReservationsFile ? 2 : 2], formatter);

                    if (date.isAfter(LocalDate.now().minusDays(30)) && !reservationStatusMap.containsKey(reservationId)) {
                        reservationStatusMap.put(reservationId, actualStatus);
                        reservationDateMap.put(reservationId, date);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading CSV file: " + filePath);
            }
        }

        // Count status occurrences
        Map<String, Integer> statusCounts = new HashMap<>();
        for (String status : reservationStatusMap.values()) {
            statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
        }

        // Populate table model
        for (Map.Entry<Integer, String> entry : reservationStatusMap.entrySet()) {
            int reservationId = entry.getKey();
            String status = entry.getValue();
            LocalDate date = reservationDateMap.get(reservationId);
            tableModel.addRow(new Object[]{reservationId, status, date});
        }

        // Create pie chart
        PieChart chart = new PieChartBuilder().width(600).height(400)
                .title("Status of reservations in the previous 30 days").build();

        // Customize chart
        chart.getStyler().setLegendPosition(PieStyler.LegendPosition.InsideNW);
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setChartTitleFont(new Font("Arial", Font.BOLD, 18));
        chart.getStyler().setLegendFont(new Font("Arial", Font.PLAIN, 14));
        chart.getStyler().setLabelsDistance(1.15);
        chart.getStyler().setPlotContentSize(0.7);

        for (Map.Entry<String, Integer> entry : statusCounts.entrySet()) {
            chart.addSeries(entry.getKey(), entry.getValue());
        }

        // Create table
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 150));

        // Add chart and table to the panel
        add(new XChartPanel<>(chart), BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }
}
