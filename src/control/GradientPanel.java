package control;

import javax.swing.*;
import java.awt.*;



public class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Gradient Colors (Customize as needed)
        Color color1 = new Color(255, 94, 87);   // Orange
        Color color2 = new Color(255, 42, 104);  // Pink
        


        // Create a vertical gradient (Top to Bottom)
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}