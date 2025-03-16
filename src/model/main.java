package model;
import javax.swing.*;

import java.awt.*;
import javax.swing.border.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import control.ButtonHoverEffect;
import control.GradientPanel;

public class main { // Renamed to follow Java conventions
    public static void main(String[] args) {
    	
        // ✅ Get Screen Size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setTitle("EXOTIC CAR RENTAL");
        ImageIcon logo = new ImageIcon("C:/Users/Tirth/Downloads/loginimg.png");
        frame.setIconImage(logo.getImage());
        frame.setLayout(null);

        
        ImageIcon backimg = new ImageIcon("C:/Users/Tirth/Downloads/loginimg.png");
        Image imgScale = backimg.getImage().getScaledInstance(1550, 900, Image.SCALE_SMOOTH);
        backimg = new ImageIcon(imgScale); 

        JLabel img = new JLabel(backimg);
        img.setBounds(0, 0, 1550, 900); 
        
        Font wlcfont = new Font("Poppins",Font.BOLD,30);
        JLabel wlc = new JLabel("Welcome To");
        wlc.setFont(wlcfont);
        wlc.setForeground(Color.WHITE); 
        wlc.setHorizontalAlignment(SwingConstants.CENTER);
        wlc.setBounds(590, 50, 400, 50); 
        frame.getLayeredPane().add(wlc, Integer.valueOf(3));
        
        Font wlcf = new Font("Poppins",Font.BOLD,55);
        JLabel wlcff = new JLabel("Exotic Car Rental");
        wlcff.setFont(wlcf);
        wlcff.setForeground(new Color(255, 215, 0, 255)); 
        wlcff.setHorizontalAlignment(SwingConstants.CENTER);
        wlcff.setBounds(550, 100, 500, 50); 
        frame.getLayeredPane().add(wlcff, Integer.valueOf(3));

        // ✅ Create Glassmorphic Login Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setBounds(600, 180, 400, 300);
        loginPanel.setOpaque(false);
        loginPanel.setLayout(new GridBagLayout());

        // ✅ GridBag Layout for Centered Elements
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ✅ Create Black Border
        Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);

        // ✅ Email Field
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JTextField emailField = new JTextField(20);
        TitledBorder emailBorder = BorderFactory.createTitledBorder(blackBorder, "Email");
        emailBorder.setTitleColor(Color.BLACK);
        Font titleFont = new Font("Poppins", Font.BOLD, 16);
        emailBorder.setTitleFont(titleFont);
        emailField.setBorder(emailBorder);
        emailField.setOpaque(false);
        emailField.setForeground(Color.BLACK);
        loginPanel.add(emailField, gbc);

        // ✅ Password Field
        gbc.gridy = 1;
        JPasswordField passwordField = new JPasswordField(20);
        TitledBorder passwordBorder = BorderFactory.createTitledBorder(blackBorder, "Password");
        
        passwordBorder.setTitleColor(Color.BLACK);
        Font passFont = new Font("Poppins", Font.BOLD, 16);
        passwordBorder.setTitleFont(passFont);
        
        passwordField.setBorder(passwordBorder);
        passwordField.setOpaque(false);
        passwordField.setForeground(Color.BLACK);
        loginPanel.add(passwordField, gbc);

     // ✅ Login Button
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JButton loginButton = new JButton("Login");
        loginButton.setFocusPainted(false);
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(100, 45)); // Increase Button Size
        loginButton.setMargin(new Insets(10, 10, 10, 10)); // Add Padding
        loginButton.setFont(new Font("Poppins", Font.BOLD, 18)); // Increase Font Size
        loginPanel.add(loginButton, gbc);
        
        // Action Listener for Login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticateUser(email, password)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                    frame.dispose(); // Close login window
                    new UserDashboard(); // Open User Dashboard
                }else if (authenticateAdmin(email, password)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                    new AdminPanel();
                    
                    
                }
                else {
                    JOptionPane.showMessageDialog(frame, "Invalid email or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        

        // ✅ Create Account Button
        gbc.gridx = 1;
        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setFocusPainted(false);
        createAccountButton.setBackground(new Color(40, 167, 69));
        createAccountButton.setForeground(Color.WHITE);
        createAccountButton.setPreferredSize(new Dimension(180, 45)); // Increase Button Size
        createAccountButton.setMargin(new Insets(10, 10, 10, 10)); // Add Padding
        createAccountButton.setFont(new Font("Poppins", Font.BOLD, 18)); // Increase Font Size
        
        loginPanel.add(createAccountButton, gbc);
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == createAccountButton) {
                    frame.dispose();
                    user us = new user();
                    
                }
            }
        });




        // ✅ Black Border for Hover Effect
        Border defaultBorder = BorderFactory.createEmptyBorder(5, 15, 5, 15);
        Border hoverBorder = BorderFactory.createLineBorder(Color.BLACK, 2);

        loginButton.setBorder(defaultBorder);
        createAccountButton.setBorder(defaultBorder);

        // ✅ Add Mouse Hover Effect
        ButtonHoverEffect.applyHoverEffect(loginButton);
        ButtonHoverEffect.applyHoverEffect(createAccountButton);
        // ✅ Add Components
        frame.getLayeredPane().add(img, Integer.valueOf(1));
        frame.getLayeredPane().add(loginPanel, Integer.valueOf(2));

        frame.setVisible(true);
        
     // ✅ Footer Panel (Semi-Transparent Black Background)
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(0, 0, 0, 150)); // Black with 150/255 opacity
        footerPanel.setBounds(0, screenHeight - 50, screenWidth, 30);
        footerPanel.setLayout(new BorderLayout());

        // ✅ Footer Label (Made with ❤️ by Tirth, Manan, Parth)
        JLabel footer = new JLabel("Made with ❤️ by Tirth, Manan, Parth");
        footer.setFont(new Font("Poppins", Font.BOLD, 16));
        footer.setForeground(Color.WHITE);
        footer.setHorizontalAlignment(SwingConstants.CENTER);

        // ✅ Add Label to Panel
        footerPanel.add(footer, BorderLayout.CENTER);

        // ✅ Add Footer Panel to Layered Pane
        frame.getLayeredPane().add(footerPanel, Integer.valueOf(3));
    }

    // ✅ Function to Authenticate User with Database
    private static boolean authenticateUser(String email, String password) {
        String url = "jdbc:mysql://localhost:3309/car_rental";
        String user = "root"; // Change if you have a different username
        String pass = ""; // Change if you have a password

        try (Connection con = DriverManager.getConnection(url, user, pass)) {
        	String query = "SELECT 1 FROM users WHERE email = ? AND password = ? AND banned = 0";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            return rs.next(); // Returns true if user exists
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    private static boolean authenticateAdmin(String email, String password) {
        String url = "jdbc:mysql://localhost:3309/car_rental";
        String user = "root"; // Change if you have a different username
        String pass = ""; // Change if you have a password

        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT * FROM admins WHERE email = ? AND password = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            return rs.next(); // Returns true if user exists
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
    


