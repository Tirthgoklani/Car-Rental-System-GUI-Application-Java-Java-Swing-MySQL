package model;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class user {
    JFrame frame;
    JTextField nameField, emailField;
    JPasswordField passwordField, confirmPasswordField;

    user() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);



        // ✅ Background Image
        ImageIcon bgi = new ImageIcon("C:/Users/Tirth/Pictures/sign.jpg");
        Image imgScale = bgi.getImage().getScaledInstance(1600, 900, Image.SCALE_SMOOTH);
        bgi = new ImageIcon(imgScale);
        JLabel imagelabel = new JLabel(bgi);
        imagelabel.setBounds(0, 0, 1600, 900);
        frame.add(imagelabel);

        // ✅ Create Glassmorphic Signup Panel
        JPanel signupPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // ✅ Draw Transparent White Background
                Color transparentWhite = new Color(255, 255, 255, 150); // 150 = Semi-transparent
                g2d.setColor(transparentWhite);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Rounded corners
            }
        };
        signupPanel.setBounds(600, 150, 400, 400);
        signupPanel.setOpaque(false);
        signupPanel.setLayout(new GridBagLayout());
        
     // ✅ Fix: Title Label
        JLabel titleLabel = new JLabel("EXOTIC CAR RENTAL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 26));
        titleLabel.setForeground(Color.BLACK);
        


        
        // ✅ GridBag Layout for Centered Elements
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;  // ✅ Span across two columns
        gbc.anchor = GridBagConstraints.CENTER; // ✅ Center align
        signupPanel.add(titleLabel, gbc);

        // ✅ Create Black Border for Fields
        Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);

        // ✅ Full Name Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        nameField = new JTextField(20);
        TitledBorder nameBorder = BorderFactory.createTitledBorder(blackBorder, "Full Name");
        nameBorder.setTitleColor(Color.BLACK);
        nameBorder.setTitleFont(new Font("Poppins", Font.BOLD, 16));
        nameField.setBorder(nameBorder);
        nameField.setOpaque(false);
        nameField.setForeground(Color.BLACK);
        signupPanel.add(nameField, gbc);

        // ✅ Email Field
        gbc.gridy = 2;
        emailField = new JTextField(20);
        TitledBorder emailBorder = BorderFactory.createTitledBorder(blackBorder, "Email");
        emailBorder.setTitleColor(Color.BLACK);
        emailBorder.setTitleFont(new Font("Poppins", Font.BOLD, 16));
        emailField.setBorder(emailBorder);
        emailField.setOpaque(false);
        emailField.setForeground(Color.BLACK);
        signupPanel.add(emailField, gbc);

        // ✅ Password Field
        gbc.gridy = 3;
        passwordField = new JPasswordField(20);
        TitledBorder passwordBorder = BorderFactory.createTitledBorder(blackBorder, "Password");
        passwordBorder.setTitleColor(Color.BLACK);
        passwordBorder.setTitleFont(new Font("Poppins", Font.BOLD, 16));
        passwordField.setBorder(passwordBorder);
        passwordField.setOpaque(false);
        passwordField.setForeground(Color.BLACK);
        signupPanel.add(passwordField, gbc);

        // ✅ Confirm Password Field
        gbc.gridy = 4;
        confirmPasswordField = new JPasswordField(20);
        TitledBorder confirmPasswordBorder = BorderFactory.createTitledBorder(blackBorder, "Confirm Password");
        confirmPasswordBorder.setTitleColor(Color.BLACK);
        confirmPasswordBorder.setTitleFont(new Font("Poppins", Font.BOLD, 16));
        confirmPasswordField.setBorder(confirmPasswordBorder);
        confirmPasswordField.setOpaque(false);
        confirmPasswordField.setForeground(Color.BLACK);
        signupPanel.add(confirmPasswordField, gbc);

        // ✅ Sign-Up Button
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        JButton signupButton = new JButton("Sign Up");
        signupButton.setFocusPainted(false);
        signupButton.setBackground(new Color(40, 167, 69)); // Green Button
        signupButton.setForeground(Color.WHITE);
        signupButton.setPreferredSize(new Dimension(180, 45)); // Bigger Button
        signupButton.setFont(new Font("Poppins", Font.BOLD, 18));
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == signupButton) {
                
                    registerUser();
                    
                }
            }
        });
        signupPanel.add(signupButton, gbc);

        // ✅ Back to Login Button
        gbc.gridx = 1;
        JButton backButton = new JButton("Back to Login");
        backButton.setFocusPainted(false);
        backButton.setBackground(new Color(0, 123, 255)); // Blue Button
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(180, 45)); // Bigger Button
        backButton.setFont(new Font("Poppins", Font.BOLD, 18));
        signupPanel.add(backButton, gbc);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == backButton) {
                	frame.dispose();
                    main.main(null);
                    
                }
            }
        });

        // ✅ Add Signup Panel to Frame
        frame.add(signupPanel);
        frame.getLayeredPane().add(signupPanel, Integer.valueOf(3));
        frame.setVisible(true);
    }


 // ✅ Register User Method (without Database class)
    private void registerUser() {
        String fullName = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(frame, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ Database Connection Details
        String dbURL = "jdbc:mysql://localhost:3309/car_rental"; // Change port if needed
        String dbUser = "root";  // Change if needed
        String dbPassword = "";   // Change if needed

        Connection con = null;
        PreparedStatement pst = null;

        try {
            // ✅ Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ Connect to Database
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            System.out.println("✅ Database connected successfully!");

            // ✅ Check if user already exists
            String checkQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
            pst = con.prepareStatement(checkQuery);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(frame, "Email already registered!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ✅ Insert User into Database
            String insertQuery = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            pst = con.prepareStatement(insertQuery);
            pst.setString(1, fullName);
            pst.setString(2, email);
            pst.setString(3, password);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                
            } else {
                JOptionPane.showMessageDialog(frame, "Sign-up failed. Try again!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "MySQL JDBC Driver not found!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            // ✅ Close resources
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
