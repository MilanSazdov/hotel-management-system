package com.hotelmanagement.view;

import javax.swing.*;
import java.awt.*;

public class LogoPanel extends JPanel {

    private Image logo;

    public LogoPanel() {
        // Load your logo image here. Make sure to adjust the path to your image.
        logo = new ImageIcon("path/to/logo.png").getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 102, 102)); // Background color
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw the logo
        int logoWidth = logo.getWidth(this);
        int logoHeight = logo.getHeight(this);
        int x = (getWidth() - logoWidth) / 2;
        int y = (getHeight() - logoHeight) / 2 - 50;
        g.drawImage(logo, x, y, this);

        // Draw the company name
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        String companyName = "Hotel Management";
        int nameWidth = g.getFontMetrics().stringWidth(companyName);
        g.drawString(companyName, (getWidth() - nameWidth) / 2, y + logoHeight + 30);
    }
}
