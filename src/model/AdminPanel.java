package model;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.*;

public class AdminPanel {
    private JFrame frame;
    private JPanel mainPanel;

    public AdminPanel() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        frame = new JFrame("Admin Panel");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(5, 1, 10, 10));
        sidebar.setPreferredSize(new Dimension(200, frame.getHeight()));
        sidebar.setBackground(new Color(40, 40, 40));

        JButton addCarButton = createSidebarButton("Add New Car");
        JButton viewUsersButton = createSidebarButton("View Users");
        JButton viewRentedCarsButton = createSidebarButton("View Rented Cars");
        JButton removeUserButton = createSidebarButton("Ban/Unban User");
        JButton logoutButton = createSidebarButton("Logout");

        sidebar.add(addCarButton);
        sidebar.add(viewUsersButton);
        sidebar.add(viewRentedCarsButton);
        sidebar.add(removeUserButton);
        sidebar.add(logoutButton);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 30));

        addCarButton.addActionListener(e -> showAddNewCarPanel());
        viewUsersButton.addActionListener(e -> showUsersPanel());
        viewRentedCarsButton.addActionListener(e -> showRentedCarsPanel());
        removeUserButton.addActionListener(e -> showBanUserPanel());
        logoutButton.addActionListener(e -> frame.dispose());

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Poppins", Font.BOLD, 16));
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void showBanUserPanel() {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Username", "Email", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setShowGrid(true);
        table.setGridColor(Color.BLACK);


        // Header Styling
        table.getTableHeader().setFont(new Font("Poppins", Font.BOLD, 25));
        table.getTableHeader().setBackground(new Color(250, 209, 119));
        table.getTableHeader().setForeground(Color.black);
        table.setFont(new Font("Poppins", Font.PLAIN, 14));
        table.setRowHeight(25);
        

        // Center Align Table Data
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/car_rental", "root", "");
            String query = "SELECT id, name, email, banned FROM users";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String status = rs.getBoolean("banned") ? "BANNED" : "ACTIVE";
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("email"), status});
            }
            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error loading users: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.setBackground(Color.LIGHT_GRAY);

        JLabel banLabel = new JLabel("Enter User ID to Ban/Unban:");
        banLabel.setFont(new Font("Poppins", Font.BOLD, 14));
        banLabel.setForeground(Color.BLACK);

        JTextField banInput = new JTextField(10);
        banInput.setFont(new Font("Poppins", Font.PLAIN, 14));
        banInput.setBackground(Color.WHITE);
        banInput.setForeground(Color.BLACK);

        JButton banButton = new JButton("Ban/Unban User");
        banButton.setFont(new Font("Poppins", Font.BOLD, 14));
        banButton.setBackground(Color.RED);
        banButton.setForeground(Color.WHITE);
        banButton.setPreferredSize(new Dimension(150, 45));

        banButton.addActionListener(e -> {
            String userId = banInput.getText();
            if (!userId.isEmpty()) {
            	toggleUserBanStatus(Integer.parseInt(userId));
                showBanUserPanel();
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a valid User ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        bottomPanel.add(banLabel);
        bottomPanel.add(banInput);
        bottomPanel.add(banButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
    }

    private void toggleUserBanStatus(int userId) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/car_rental", "root", "");
            String checkQuery = "SELECT banned FROM users WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                boolean isBanned = rs.getBoolean("banned");
                String updateQuery = "UPDATE users SET banned = ? WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setBoolean(1, !isBanned);
                updateStmt.setInt(2, userId);
                int rowsAffected = updateStmt.executeUpdate();
                conn.close();

                if (rowsAffected > 0) {
                    String message = isBanned ? "User unbanned successfully!" : "User banned successfully!";
                    JOptionPane.showMessageDialog(frame, message, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "User ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "User ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error updating user status: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void showAddNewCarPanel() {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout(20, 20));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 10)); // Top, Left, Bottom, Right

        JPanel imagePanel = new JPanel(new BorderLayout());
        

        JTextField brandField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField colorField = new JTextField();
        
        // Dropdown for Fuel Type
        String[] fuelTypes = {"Petrol", "Diesel", "Electric", "Hybrid"};
        JComboBox<String> fuelTypeDropdown = new JComboBox<>(fuelTypes);

        JTextField seatsField = new JTextField();
        JTextField priceField = new JTextField();
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(500, 200)); // Bigger preview

        String[] selectedImagePath = new String[1];

        Font labelFont1 = new Font("Poppins", Font.BOLD, 30); // Increased font size

        JLabel brandLabel = new JLabel("Brand:");
        brandLabel.setFont(labelFont1);
        formPanel.add(brandLabel);
        formPanel.add(brandField);

        JLabel modelLabel = new JLabel("Model:");
        modelLabel.setFont(labelFont1);
        formPanel.add(modelLabel);
        formPanel.add(modelField);

        JLabel colorLabel = new JLabel("Color:");
        colorLabel.setFont(labelFont1);
        formPanel.add(colorLabel);
        formPanel.add(colorField);

        JLabel fuelTypeLabel = new JLabel("Fuel Type:");
        fuelTypeLabel.setFont(labelFont1);
        formPanel.add(fuelTypeLabel);
        formPanel.add(fuelTypeDropdown);  // Updated

        JLabel seatsLabel = new JLabel("Seats:");
        seatsLabel.setFont(labelFont1);
        formPanel.add(seatsLabel);
        formPanel.add(seatsField);

        JLabel priceLabel = new JLabel("Price per Day:");
        priceLabel.setFont(labelFont1);
        formPanel.add(priceLabel);
        formPanel.add(priceField);


        JButton selectImageButton = new JButton("Select Image");
        JButton saveButton = new JButton("Save Car");

        // Set preferred size
        selectImageButton.setPreferredSize(new Dimension(150, 50));
        saveButton.setPreferredSize(new Dimension(150, 50));

        // Change button colors
        selectImageButton.setBackground(new Color(30, 144, 255)); // Dodger Blue
        selectImageButton.setForeground(Color.WHITE); // White text

        saveButton.setBackground(new Color(50, 205, 50)); // Lime Green
        saveButton.setForeground(Color.WHITE); // White text

        // Set bold font for better visibility
        Font buttonFont = new Font("Poppins", Font.BOLD, 14);
        selectImageButton.setFont(buttonFont);
        saveButton.setFont(buttonFont);

        selectImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                selectedImagePath[0] = selectedFile.getAbsolutePath();
                ImageIcon icon = new ImageIcon(new ImageIcon(selectedImagePath[0]).getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH));
                imageLabel.setIcon(icon);
            }
        });

        saveButton.addActionListener(e -> {
            String brand = brandField.getText();
            String model = modelField.getText();
            String color = colorField.getText();
            String fuelType = (String) fuelTypeDropdown.getSelectedItem(); // Fetch selected fuel type
            String seats = seatsField.getText();
            String price = priceField.getText();

            if (brand.isEmpty() || model.isEmpty() || color.isEmpty() || seats.isEmpty() || price.isEmpty() || selectedImagePath[0] == null) {
                JOptionPane.showMessageDialog(frame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                saveCarToDatabase(brand, model, color, fuelType, seats, price, selectedImagePath[0]);
            }
        });

        // Button panel for select image and save car
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectImageButton);
        buttonPanel.add(saveButton);

        imagePanel.add(imageLabel, BorderLayout.CENTER);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(imagePanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
    }



    private void saveCarToDatabase(String brand, String model, String color, String fuelType, String seats, String price, String imagePath) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/car_rental", "root", "");
            String query = "INSERT INTO cars (brand, model, color, fuel_type, seats, price_per_day, image_path, availability) VALUES (?, ?, ?, ?, ?, ?, ?, 'Available')";
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setString(1, brand);
            stmt.setString(2, model);
            stmt.setString(3, color);
            stmt.setString(4, fuelType);  // Now taking from JComboBox
            stmt.setInt(5, Integer.parseInt(seats));
            stmt.setDouble(6, Double.parseDouble(price));
            stmt.setString(7, imagePath);
            
            stmt.executeUpdate();
            conn.close();
            
            JOptionPane.showMessageDialog(frame, "Car added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error adding car: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void showUsersPanel() {
        mainPanel.removeAll(); // Clear previous content
        mainPanel.setLayout(new BorderLayout());

        // Column names for the table
        String[] columnNames = {"ID", "Username", "Email"};
        
        // Table model to hold data
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable usersTable = new JTable(tableModel);
        usersTable.setRowHeight(30); // Increase row height
        usersTable.setFont(new Font("Poppins", Font.PLAIN, 16)); // Set font size
        usersTable.setForeground(Color.white);

        // Adjust column widths
        usersTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        usersTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Username
        usersTable.getColumnModel().getColumn(2).setPreferredWidth(300); // Email

        // Customize table header
        JTableHeader tableHeader = usersTable.getTableHeader();
        tableHeader.setFont(new Font("Poppins", Font.BOLD, 25)); // Bold font for header
        tableHeader.setBackground(new Color(250, 209, 119)); // Dark gray background
        tableHeader.setForeground(Color.black); // White text color

        // Create a cell renderer to center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Apply centering to all columns
        for (int i = 0; i < usersTable.getColumnCount(); i++) {
            usersTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/car_rental", "root", "");
            String query = "SELECT id, name, email FROM users";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                tableModel.addRow(new Object[]{id, name, email});
            }
            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error loading users: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // Wrap table inside a scroll pane
        JScrollPane scrollPane = new JScrollPane(usersTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Refresh UI
        frame.revalidate();
        frame.repaint();
    }
    private void showRentedCarsPanel() {
        mainPanel.removeAll();
        JTextArea rentedCarsList = new JTextArea(10, 30);
        rentedCarsList.setEditable(false);

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/car_rental", "root", "");
            String query = "SELECT users.name, cars.brand, cars.model FROM rentals " +
                    "JOIN users ON rentals.user_id = users.id " +
                    "JOIN cars ON rentals.car_id = cars.id";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rentedCarsList.append("User: " + rs.getString("name") + " | Car: " + rs.getString("brand") + " " + rs.getString("model") + "\n");
            }
            conn.close();
        } catch (Exception ex) {
            rentedCarsList.setText("Error loading rentals: " + ex.getMessage());
        }

        mainPanel.add(new JScrollPane(rentedCarsList));
        frame.revalidate();
        frame.repaint();
    }


    public static void main(String[] args) {
        new AdminPanel();
    }
}
