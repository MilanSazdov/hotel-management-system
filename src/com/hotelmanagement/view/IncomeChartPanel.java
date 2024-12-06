package com.hotelmanagement.view;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.List;

import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.RoomType;

public class IncomeChartPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public IncomeChartPanel() {
        setLayout(new BorderLayout());

        // Load data
        Map<String, double[]> incomeData = new HashMap<>();
        double[] totalIncome = new double[12];
        double overallTotalIncome = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Initialize room types
        List<RoomType> roomTypes = RoomTypeController.getInstance().getRoomTypeList();
        for (RoomType roomType : roomTypes) {
            incomeData.put(roomType.getCategory().name(), new double[12]);
        }

        try (BufferedReader br = new BufferedReader(new FileReader("src/com/hotelmanagement/data/confirmed_reservations_by_period.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int reservationId = Integer.parseInt(parts[1]);
                LocalDate date = LocalDate.parse(parts[2], formatter);

                if (date.isAfter(LocalDate.now().minusMonths(12))) {
                    Reservation reservation = ReservationController.getInstance().findReservationById(reservationId);
                    if (reservation != null) {
                        int monthIndex = (12 + date.getMonthValue() - LocalDate.now().minusMonths(11).getMonthValue()) % 12;
                        double cost = reservation.getTotalCost();
                        String roomTypeName = RoomTypeController.getInstance().searchRoomTypeById(reservation.getRoomTypeId()).getCategory().name();

                        incomeData.get(roomTypeName)[monthIndex] += cost;
                        totalIncome[monthIndex] += cost;
                        overallTotalIncome += cost; // Accumulate total income
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading CSV file.");
        }

        // Create chart
        CategoryChart chart = new CategoryChartBuilder().width(800).height(600)
                .title("Income by Room Type").xAxisTitle("Month").yAxisTitle("Income").build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
        chart.getStyler().setLegendPadding(10);
        chart.getStyler().setLegendBackgroundColor(Color.LIGHT_GRAY);
        chart.getStyler().setChartTitleFont(new Font("Arial", Font.BOLD, 18));
        chart.getStyler().setLegendFont(new Font("Arial", Font.PLAIN, 14));
        chart.getStyler().setXAxisLabelRotation(45);

        // Months labels
        List<String> months = IntStream.range(0, 12)
                .mapToObj(i -> LocalDate.now().minusMonths(11 - i).getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH))
                .collect(Collectors.toList());

        // Add series
        for (Map.Entry<String, double[]> entry : incomeData.entrySet()) {
            chart.addSeries(entry.getKey(), months, Arrays.stream(entry.getValue()).boxed().collect(Collectors.toList()));
        }
        chart.addSeries("Total", months, Arrays.stream(totalIncome).boxed().collect(Collectors.toList()));

        // Add chart to panel
        add(new XChartPanel<>(chart), BorderLayout.CENTER);

        // Add total income label
        JLabel totalIncomeLabel = new JLabel(String.format("Total income: %.2f", overallTotalIncome));
        totalIncomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalIncomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(totalIncomeLabel, BorderLayout.SOUTH);
    }
}
