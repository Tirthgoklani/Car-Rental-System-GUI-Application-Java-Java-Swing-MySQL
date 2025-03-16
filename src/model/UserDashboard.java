package model;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDashboard {
    private JFrame frame;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    
    // Database connection properties
    private static final String DB_URL = "jdbc:mysql://localhost:3309/car_rental";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    // Dark theme colors
    private Color primaryColor = new Color(33, 150, 243);    // Blue accent
    private Color accentColor = new Color(255, 59, 48);      // Red accent
    private Color darkBgColor = new Color(18, 18, 18);       // Very dark background
    private Color darkCardColor = new Color(30, 30, 30);     // Dark card background
    private Color darkSecondaryColor = new Color(66, 66, 66); // Secondary background
    private Color textColor = new Color(240, 240, 240);      // Light text
    private Color secondaryTextColor = new Color(180, 180, 180); // Secondary text

    public UserDashboard() {
        setupLookAndFeel();
        buildInterface();
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Set UI manager properties for dark theme
            UIManager.put("Panel.background", darkBgColor);
            UIManager.put("OptionPane.background", darkBgColor);
            UIManager.put("OptionPane.messageForeground", textColor);
            UIManager.put("Button.background", darkSecondaryColor);
            UIManager.put("Button.foreground", textColor);
            UIManager.put("Label.foreground", textColor);
            UIManager.put("TextField.background", darkSecondaryColor);
            UIManager.put("TextField.foreground", textColor);
            UIManager.put("ComboBox.background", darkSecondaryColor);
            UIManager.put("ComboBox.foreground", textColor);
            UIManager.put("ScrollPane.background", darkBgColor);
            UIManager.put("ComboBox.background", darkSecondaryColor);
            UIManager.put("ComboBox.foreground", textColor);
            UIManager.put("ComboBox.selectionBackground", primaryColor);
            UIManager.put("ComboBox.selectionForeground", textColor);
            
            // Modern rounded corners
            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 15);
            UIManager.put("ProgressBar.arc", 15);
            UIManager.put("TextComponent.arc", 15);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void buildInterface() {
        frame = new JFrame("Exotic Car Rental");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(darkBgColor);
        
        createNavBar();
        createCarListingPanel();
        
        frame.setVisible(true);
    }
    
    private void createNavBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(21, 21, 21)); // Slightly lighter than background
        navBar.setPreferredSize(new Dimension(frame.getWidth(), 80)); // Taller navbar
        navBar.setBorder(new EmptyBorder(15, 40, 15, 40)); // More padding
        
        // App title with modern font
        JLabel title = new JLabel("EXOTIC CAR RENTAL");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(primaryColor);
        
        // Modern back button
        JButton logoutButton = createStyledButton("Log Out", accentColor, Color.WHITE);
        logoutButton.setPreferredSize(new Dimension(140, 40));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            // Show message for exit functionality
            JOptionPane.showMessageDialog(null, "Logged Out Successfully");
            main.main(null);
            
        });
        
     // New button: View your rentals
        JButton viewRentalsButton = createStyledButton("Your Rentals", primaryColor, textColor);
        viewRentalsButton.setPreferredSize(new Dimension(140, 40)); // Adjust width & height

        viewRentalsButton.addActionListener(e -> {
            showRentalsPanel();
        });
        viewRentalsButton.setHorizontalAlignment(SwingConstants.CENTER);
        viewRentalsButton.setVerticalAlignment(SwingConstants.CENTER);

        logoutButton.setHorizontalAlignment(SwingConstants.CENTER);
        logoutButton.setVerticalAlignment(SwingConstants.CENTER);


        // Add buttons to a panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(viewRentalsButton);
        buttonPanel.add(logoutButton);

        navBar.add(title, BorderLayout.WEST);
        navBar.add(buttonPanel, BorderLayout.EAST);

        frame.add(navBar, BorderLayout.NORTH);
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false); // Remove focus border
        button.setBorderPainted(false); // Remove border
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        button.setPreferredSize(new Dimension(120, 50)); // Larger button size
        return button;
    }
    
    private void createCarListingPanel() {
        // Create a container with padding
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(darkBgColor);
        
        // Car listing grid with gap between cards
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 3, 40, 40)); // Increased spacing between cards
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40)); // More padding
        mainPanel.setBackground(darkBgColor);
        
        loadAvailableCars();
        
        containerPanel.add(mainPanel);
        
        // Improved scroll pane
        scrollPane = new JScrollPane(containerPanel);
        scrollPane.setBorder(null); // Remove border
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(darkBgColor);
        
        // Customize scrollbar for dark theme
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = darkSecondaryColor;
                this.trackColor = darkBgColor;
            }
        });
        
        frame.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadAvailableCars() {
        try {
            List<Car> cars = getCarsFromDatabase();
            
            if (cars.isEmpty()) {
                showErrorDialog("No cars available at the moment.");
                return;
            }
            
            // Create car panels for each car from the database
            for (Car car : cars) {
                JPanel carPanel = createCarPanel(car);
                mainPanel.add(carPanel);
            }
            
            // Adjust height dynamically based on number of cars
            int rows = (int) Math.ceil(cars.size() / 3.0);
            int panelHeight = rows * 700; // Increased height for larger cards
            mainPanel.setPreferredSize(new Dimension(1200, panelHeight));
            
        } catch (SQLException ex) {
            showErrorDialog("Database error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            showErrorDialog("Error loading cars: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private List<Car> getCarsFromDatabase() throws SQLException {
        List<Car> cars = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM cars WHERE availability = 'Available'")) {
            
            while (rs.next()) {
                Car car = new Car(
                    rs.getInt("id"),
                    rs.getString("brand"),
                    rs.getString("model"),
                    rs.getString("color"),
                    rs.getInt("seats"),
                    rs.getDouble("price_per_day"),
                    rs.getString("image_path"),
                    "Available".equals(rs.getString("availability"))  // Convert availability string to boolean
                );
                cars.add(car);
            }
        }
        
        return cars;
    }
    
    private JPanel createCarPanel(Car car) {
        // Modern card layout with rounded corners and shadow effect
        JPanel carPanel = new JPanel(new BorderLayout());
        carPanel.setBackground(darkCardColor);
        
        // Add shadow and rounded corners with dark theme
        carPanel.setBorder(new CompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED, 
                new Color(45, 45, 45), // Slightly lighter
                new Color(15, 15, 15)  // Slightly darker
            ),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Car image panel - SIGNIFICANTLY LARGER
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(darkCardColor);
        imagePanel.setPreferredSize(new Dimension(500, 350)); // Much larger image area
        
        // Create image from path or placeholder if not available
        JPanel carImagePanel;
        if (car.getImagePath() != null && !car.getImagePath().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(car.getImagePath());
                Image img = icon.getImage().getScaledInstance(500, 350, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(img));
                carImagePanel = new JPanel(new BorderLayout());
                carImagePanel.add(imageLabel, BorderLayout.CENTER);
                carImagePanel.setBackground(darkCardColor);
            } catch (Exception e) {
                // Fallback to placeholder if image can't be loaded
                carImagePanel = createPlaceholderImage(car.getBrand(), car.getModel());
            }
        } else {
            carImagePanel = createPlaceholderImage(car.getBrand(), car.getModel());
        }
        
        carImagePanel.setPreferredSize(new Dimension(550, 350));
        imagePanel.add(carImagePanel, BorderLayout.CENTER);
        
        // Info panel with cleaner layout
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(darkCardColor);
        infoPanel.setBorder(new EmptyBorder(20, 15, 20, 15));
        
        // Title - Car model and brand with bigger font
        JLabel carTitle = new JLabel(car.getBrand() + " " + car.getModel());
        carTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        carTitle.setForeground(textColor);
        carTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Details panel
        JPanel detailsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        detailsPanel.setBackground(darkCardColor);
        detailsPanel.setBorder(new EmptyBorder(15, 0, 15, 0));
        
        // Create styled detail labels
        addDetailRow(detailsPanel, "Color:", car.getColor());
        addDetailRow(detailsPanel, "Seats:", String.valueOf(car.getSeats()));
        addDetailRow(detailsPanel, "Price:", "₹" + car.getPricePerDay() + "/day");
        
        // Create a modern "Rent Now" button with animated hover effect
        JButton rentButton = createStyledButton("Rent Now", primaryColor, textColor);
        rentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rentButton.setMaximumSize(new Dimension(200, 50)); // Larger button
        
        // Add hover effect
        rentButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                rentButton.setBackground(primaryColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                rentButton.setBackground(primaryColor);
            }
        });
        
        final String carName = car.getBrand() + " " + car.getModel();
        final int carId = car.getId();
        
        rentButton.addActionListener(e -> {
            showRentDialog(carName, carId);
        });
        
        // Add components to info panel
        infoPanel.add(carTitle);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(detailsPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(rentButton);
        
        // Add components to car panel
        carPanel.add(imagePanel, BorderLayout.CENTER);
        carPanel.add(infoPanel, BorderLayout.SOUTH);
        
        return carPanel;
    }
    
    private JPanel createPlaceholderImage(String brand, String model) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Use a gradient background to simulate a car image
                GradientPaint gp = new GradientPaint(0, 0, 
                                     new Color(50, 50, 70), 
                                     getWidth(), getHeight(), 
                                     new Color(25, 25, 35));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw a car silhouette
                g2d.setColor(new Color(90, 90, 120, 200));
                int[] xPoints = {
                    getWidth()/10, getWidth()/4, getWidth()/2, 3*getWidth()/4, 9*getWidth()/10,
                    9*getWidth()/10, 3*getWidth()/4, getWidth()/4, getWidth()/10
                };
                int[] yPoints = {
                    getHeight()/2, getHeight()/3, getHeight()/4, getHeight()/3, getHeight()/2,
                    2*getHeight()/3, 3*getHeight()/4, 3*getHeight()/4, 2*getHeight()/3
                };
                g2d.fillPolygon(xPoints, yPoints, xPoints.length);
                
                // Draw wheels
                g2d.setColor(Color.BLACK);
                g2d.fillOval(getWidth()/4 - 20, 2*getHeight()/3, 40, 40);
                g2d.fillOval(3*getWidth()/4 - 20, 2*getHeight()/3, 40, 40);
                
                // Draw brand name
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 24));
                FontMetrics fm = g2d.getFontMetrics();
                String brandText = brand + " " + model;
                int textWidth = fm.stringWidth(brandText);
                g2d.drawString(brandText, (getWidth() - textWidth) / 2, getHeight() / 2);
                
                g2d.dispose();
            }
        };
    }
    
    private void addDetailRow(JPanel panel, String label, String value) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        labelComp.setForeground(secondaryTextColor);
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueComp.setForeground(textColor);
        
        panel.add(labelComp);
        panel.add(valueComp);
    }
    
    private void showRentDialog(String carDetails, int carId) {
        JDialog rentDialog = new JDialog(frame, "Rent Car", true);
        rentDialog.setSize(500, 500); // Larger dialog
        rentDialog.setLocationRelativeTo(frame);
        rentDialog.setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(darkCardColor);
        
        // Header
        JLabel headerLabel = new JLabel("Complete Your Rental");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(primaryColor);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Car info
        JLabel carLabel = new JLabel("Selected Vehicle: " + carDetails);
        carLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        carLabel.setForeground(textColor);
        carLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 15, 20));
        formPanel.setBackground(darkCardColor);
        formPanel.setBorder(new EmptyBorder(25, 0, 25, 0));
        
        // Create date pickers with dark theme styling
        JLabel pickupLabel = new JLabel("Pickup Date:");
        pickupLabel.setForeground(textColor);
        pickupLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JFormattedTextField pickupDate = createStyledDateField();
        
        JLabel returnLabel = new JLabel("Return Date:");
        returnLabel.setForeground(textColor);
        returnLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JFormattedTextField returnDate = createStyledDateField();
        
        JLabel paymentLabel = new JLabel("Payment Method:");
        paymentLabel.setForeground(textColor);
        paymentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        String[] paymentOptions = {"Credit Card", "Debit Card", "PayPal"};
        JComboBox<String> paymentCombo = createStyledComboBox(paymentOptions);

        JLabel insuranceLabel = new JLabel("Insurance Type:");
        insuranceLabel.setForeground(textColor);
        insuranceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        String[] insuranceOptions = {"Basic", "Premium", "Full Coverage"};
        JComboBox<String> insuranceCombo = createStyledComboBox(insuranceOptions);
        
        // Add components to form panel
        formPanel.add(pickupLabel);
        formPanel.add(pickupDate);
        formPanel.add(returnLabel);
        formPanel.add(returnDate);
        formPanel.add(paymentLabel);
        formPanel.add(paymentCombo);
        formPanel.add(insuranceLabel);
        formPanel.add(insuranceCombo);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(darkCardColor);
        
        // Cancel button
        JButton cancelButton = createStyledButton("Cancel", darkSecondaryColor, textColor);
        cancelButton.addActionListener(e -> rentDialog.dispose());
        
        // Confirm button
        JButton confirmButton = createStyledButton("Confirm", primaryColor, textColor);
        confirmButton.addActionListener(e -> {
            try {
                // Format dates for database
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                String pickupDateStr = dbFormat.format(((Date)pickupDate.getValue()));
                String returnDateStr = dbFormat.format(((Date)returnDate.getValue()));
                String paymentMethod = (String)paymentCombo.getSelectedItem();
                String insurance = (String)insuranceCombo.getSelectedItem();
                
                // Create rental in database
                boolean success = createRentalInDatabase(
                    carId, 
                    pickupDateStr, 
                    returnDateStr, 
                    paymentMethod, 
                    insurance
                );
                
                if (success) {
                    // Update car availability in database
                    updateCarAvailability(carId, false);
                    showConfirmation("Booking Confirmed!", "You have successfully booked the " + carDetails);
                } else {
                    showErrorDialog("Failed to create rental. Please try again.");
                }
                
                rentDialog.dispose();
            } catch (Exception ex) {
                showErrorDialog("Error processing rental: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        
        // Add buttons to panel
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
        
        // Add all components to content panel
        contentPanel.add(headerLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(carLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(buttonPanel);
        
        rentDialog.add(contentPanel, BorderLayout.CENTER);
        rentDialog.setVisible(true);
    }
    
    private boolean createRentalInDatabase(int carId, String pickupDate, String returnDate, 
                                          String paymentMethod, String insurance) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Assuming you have a users table and the current user is identified
            int userId = 1; // This would normally come from a logged-in user session
            
            String sql = "INSERT INTO rentals (car_id, user_id, pickup_date, return_date, " +
                         "payment_method, insurance, status) VALUES (?, ?, ?, ?, ?, ?, 'CONFIRMED')";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, carId);
                pstmt.setInt(2, userId);
                pstmt.setString(3, pickupDate);
                pstmt.setString(4, returnDate);
                pstmt.setString(5, paymentMethod);
                pstmt.setString(6, insurance);
                
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean updateCarAvailability(int carId, boolean available) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE cars SET availability = ? WHERE id = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Convert boolean to the appropriate ENUM value
                pstmt.setString(1, available ? "Available" : "Rented");
                pstmt.setInt(2, carId);
                
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private JFormattedTextField createStyledDateField() {
        // Create a formatted text field for dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        JFormattedTextField dateField = new JFormattedTextField(dateFormat);
        dateField.setValue(new Date()); // Set current date as default
        dateField.setBackground(darkSecondaryColor);
        dateField.setForeground(textColor);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return dateField;
    }
    
    private void showConfirmation(String title, String message) {
        JDialog successDialog = new JDialog(frame, "Confirmation", true);
        successDialog.setSize(600, 500);
        successDialog.setLocationRelativeTo(frame);
        successDialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(darkCardColor);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Success icon (checkmark simulation)
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw circle
                g2d.setColor(new Color(39, 174, 96));
                g2d.fillOval(0, 0, 60, 60);

                // Draw checkmark
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(4));
                g2d.drawLine(15, 30, 25, 45);
                g2d.drawLine(25, 45, 45, 15);

                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(60, 60);
            }
        };
        iconPanel.setBackground(darkCardColor);
        iconPanel.setPreferredSize(new Dimension(100, 70));

        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Success messages
        JLabel successLabel = new JLabel(title);
        successLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        successLabel.setForeground(primaryColor);
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel detailsLabel = new JLabel(message);
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        detailsLabel.setForeground(textColor);
        detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Close button
        JButton closeButton = createStyledButton("Close", primaryColor, textColor);
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setMaximumSize(new Dimension(150, 50));
        closeButton.addActionListener(e -> {
            successDialog.dispose();
            refreshCarListings(); // Refresh the car listings after action
        });

        // Add components to panel
        panel.add(Box.createVerticalGlue());
        panel.add(iconPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(successLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(detailsLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(closeButton);
        panel.add(Box.createVerticalGlue());

        successDialog.add(panel, BorderLayout.CENTER);
        successDialog.setVisible(true);
    }
    private void refreshCarListings() {
        // Clear the current listings
        mainPanel.removeAll();
        
        // Reload available cars from database
        loadAvailableCars();
        
        // Refresh the UI
        mainPanel.revalidate();
        mainPanel.repaint();
        scrollPane.revalidate();
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
            frame,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    private JComboBox<String> createStyledComboBox(String[] options) {
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setBackground(darkSecondaryColor);
        comboBox.setForeground(textColor);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        // Set the renderer for the items
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                         boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBackground(isSelected ? primaryColor : darkSecondaryColor);
                c.setForeground(textColor);
                return c;
            }
        });
        
        // Apply custom styling to the arrow button
        for (Component comp : comboBox.getComponents()) {
            if (comp instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) comp;
                button.setBackground(darkSecondaryColor);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
            }
        }
        
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        return comboBox;
    }
    private List<Car> getRentedCarsFromDatabase() throws SQLException {
        List<Car> rentedCars = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            int userId = 1; // Assuming user_id is 1
            String sql = "SELECT c.* FROM cars c JOIN rentals r ON c.id = r.car_id " +
                         "WHERE r.user_id = ? AND r.status = 'CONFIRMED'";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Car car = new Car(
                            rs.getInt("id"),
                            rs.getString("brand"),
                            rs.getString("model"),
                            rs.getString("color"),
                            rs.getInt("seats"),
                            rs.getDouble("price_per_day"),
                            rs.getString("image_path"),
                            false // Since they are rented
                        );
                        rentedCars.add(car);
                    }
                }
            }
        }
        return rentedCars;
    }

    private boolean returnCar(int carId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            int userId = 1; // Assuming user_id is 1
            String sql = "UPDATE rentals SET return_date = CURRENT_DATE(), status = 'Returned' " +
                         "WHERE car_id = ? AND user_id = ? AND status = 'CONFIRMED'";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, carId);
                pstmt.setInt(2, userId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Update car availability
                    updateCarAvailability(carId, true);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private JPanel createRentedCarPanel(Car car) {
        JPanel carPanel = new JPanel(new BorderLayout());
        carPanel.setBackground(darkCardColor);

        // Add shadow and rounded corners with dark theme
        carPanel.setBorder(new CompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED,
                new Color(45, 45, 45), // Slightly lighter
                new Color(15, 15, 15)  // Slightly darker
            ),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // Car image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(darkCardColor);
        imagePanel.setPreferredSize(new Dimension(500, 350));

        // Create image from path or placeholder if not available
        JPanel carImagePanel;
        if (car.getImagePath() != null && !car.getImagePath().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(car.getImagePath());
                Image img = icon.getImage().getScaledInstance(500, 350, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(img));
                carImagePanel = new JPanel(new BorderLayout());
                carImagePanel.add(imageLabel, BorderLayout.CENTER);
                carImagePanel.setBackground(darkCardColor);
            } catch (Exception e) {
                carImagePanel = createPlaceholderImage(car.getBrand(), car.getModel());
            }
        } else {
            carImagePanel = createPlaceholderImage(car.getBrand(), car.getModel());
        }

        carImagePanel.setPreferredSize(new Dimension(550, 350));
        imagePanel.add(carImagePanel, BorderLayout.CENTER);

        // Info panel with cleaner layout
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(darkCardColor);
        infoPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Title - Car model and brand with bigger font
        JLabel carTitle = new JLabel(car.getBrand() + " " + car.getModel());
        carTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        carTitle.setForeground(textColor);
        carTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Details panel
        JPanel detailsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        detailsPanel.setBackground(darkCardColor);
        detailsPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        addDetailRow(detailsPanel, "Color:", car.getColor());
        addDetailRow(detailsPanel, "Seats:", String.valueOf(car.getSeats()));
        addDetailRow(detailsPanel, "Price:", "₹" + car.getPricePerDay() + "/day");

        // Create a modern "Return Car" button with animated hover effect
        JButton returnButton = createStyledButton("Return Car", accentColor, textColor);
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.setMaximumSize(new Dimension(200, 50));

        // Add hover effect
        returnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                returnButton.setBackground(accentColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                returnButton.setBackground(accentColor);
            }
        });

        final int carId = car.getId();

        returnButton.addActionListener(e -> {
            if (returnCar(carId)) {
                showConfirmation("Return Confirmed!", "You have returned the " + car.getBrand() + " " + car.getModel());
            } else {
                showErrorDialog("Failed to return the car. Please try again.");
            }
        });

        // Add components to info panel
        infoPanel.add(carTitle);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(detailsPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(returnButton);

        // Add components to car panel
        carPanel.add(imagePanel, BorderLayout.CENTER);
        carPanel.add(infoPanel, BorderLayout.SOUTH);

        return carPanel;
    }

    private void showRentalsPanel() {
        JFrame rentalsFrame = new JFrame("Your Rentals");
        rentalsFrame.setSize(1200, 900);
        rentalsFrame.setLocationRelativeTo(frame);
        rentalsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 🔹 Navigation Bar (Title + Back Button)
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setPreferredSize(new Dimension(1000, 50));
        navBar.setBackground(primaryColor);

        // 🔹 Back Button (Redirect to Dashboard)
        JButton logoutButton = createStyledButton("Back", primaryColor, textColor);
        logoutButton.setPreferredSize(new Dimension(100, 40));
        logoutButton.addActionListener(e -> {
            rentalsFrame.dispose();  // Close rentals panel
  
        });

        // 🔹 Title Label
        JLabel titleLabel = new JLabel("Your Rentals", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        navBar.add(logoutButton, BorderLayout.WEST);
        navBar.add(titleLabel, BorderLayout.CENTER);

        // 🔹 Container Panel
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(darkBgColor);

        // 🔹 Rentals Panel (Grid Layout)
        JPanel rentalsPanel = new JPanel(new GridLayout(0, 2, 20, 40));
        rentalsPanel.setBackground(darkBgColor);

        try {
            List<Car> rentedCars = getRentedCarsFromDatabase();
            if (rentedCars.isEmpty()) {
                JLabel noRentalsLabel = new JLabel("No cars rented.");
                noRentalsLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
                noRentalsLabel.setForeground(textColor);
                noRentalsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                containerPanel.add(noRentalsLabel);
            } else {
                for (Car car : rentedCars) {
                    JPanel carPanel = createRentedCarPanel(car);
                    rentalsPanel.add(carPanel);
                }
                containerPanel.add(rentalsPanel);
            }
        } catch (SQLException ex) {
            showErrorDialog("Database error: " + ex.getMessage());
            ex.printStackTrace();
        }


        // 🔹 Scroll Pane for Rentals
        JScrollPane scrollPane = new JScrollPane(containerPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(darkBgColor);

        // Custom Scroll Bar
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = darkSecondaryColor;
                this.trackColor = darkBgColor;
            }
        });

        // 🔹 Add Components to Frame
        rentalsFrame.add(navBar, BorderLayout.NORTH);
        rentalsFrame.add(scrollPane, BorderLayout.CENTER);
        rentalsFrame.setVisible(true);
    }


    public static void main(String[] args) {
        // Set up database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found");
            e.printStackTrace();
            return;
        }
        
        // Launch the UI
        SwingUtilities.invokeLater(() -> {
            new UserDashboard();
        });
    }
}