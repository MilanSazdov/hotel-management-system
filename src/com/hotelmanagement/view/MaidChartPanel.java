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

import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.model.Maid;

public class MaidChartPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public MaidChartPanel() {
        setLayout(new BorderLayout());

        // Load data
        Map<String, Integer> maidCounts = new HashMap<>();
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Username", "Room ID", "Date"}, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader("src/com/hotelmanagement/data/cleaned_rooms_by_period.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int maidId = Integer.parseInt(parts[0]);
                String roomId = parts[1];
                LocalDate date = LocalDate.parse(parts[2], formatter);

                if (date.isAfter(LocalDate.now().minusDays(30))) {
                    Maid maid = MaidController.getInstance().getMaidById(maidId);
                    if (maid != null) {
                        String username = maid.getUsername();
                        maidCounts.put(username, maidCounts.getOrDefault(username, 0) + 1);
                        tableModel.addRow(new Object[]{username, roomId, date});
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading CSV file.");
        }

        // Create pie chart
        PieChart chart = new PieChartBuilder().width(600).height(400)
                .title("Workload of maids in the previous 30 days").build();

        // Customize chart
        chart.getStyler().setLegendPosition(PieStyler.LegendPosition.InsideNW);
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setChartTitleFont(new Font("Arial", Font.BOLD, 18));
        chart.getStyler().setLegendFont(new Font("Arial", Font.PLAIN, 14));
        chart.getStyler().setLabelsDistance(1.15);
        chart.getStyler().setPlotContentSize(0.7);

        for (Map.Entry<String, Integer> entry : maidCounts.entrySet()) {
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
