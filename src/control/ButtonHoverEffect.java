package control;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonHoverEffect {
    public static void applyHoverEffect(JButton button) {
        Border defaultBorder = BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 2); // Invisible border with same thickness
        Border hoverBorder = BorderFactory.createLineBorder(Color.BLACK, 2); // Black border on hover

        button.setBorder(defaultBorder); // Set invisible border initially

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorder(hoverBorder); // Apply black border on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(defaultBorder); // Revert to invisible border
            }
        });
    }
}
