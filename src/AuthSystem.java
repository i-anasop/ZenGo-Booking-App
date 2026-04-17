import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class AuthSystem extends JFrame {
    private CardLayout cardLayout;
    private JPanel rightPanel;
    private JPanel imagePanel;

    private final HashMap<String, String> users = new HashMap<>();

    public AuthSystem() {

        setTitle("ZEN | Authentication");
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icon.png"));
        setIconImage(icon.getImage());
        setSize(1200, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Load users from database
        loadUsersFromFile();

        // Image Panel
        imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(21, 101, 192));
        imagePanel.add(createImageLabel("/resources/login.png"), BorderLayout.CENTER);
        imagePanel.setPreferredSize(new Dimension(600, 800));
        add(imagePanel, BorderLayout.WEST);

        // Right Panel (Login / Signup)
        rightPanel = new JPanel();
        cardLayout = new CardLayout();
        rightPanel.setLayout(cardLayout);
        rightPanel.add(createLoginPanel(), "login");
        rightPanel.add(createSignupPanel(), "signup");

        add(rightPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JLabel createImageLabel(String path) {
        JLabel label = new JLabel(new ImageIcon(getClass().getResource(path)));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel title = new JLabel("LOGIN");
        title.setFont(new Font("SansSerif", Font.BOLD, 48));
        title.setForeground(new Color(33, 47, 61));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        JTextField emailField = new JTextField(18);
        JPasswordField passwordField = new JPasswordField(18);
        JCheckBox showPass = new JCheckBox("Show Password");

        // Error message label below inputs
        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);

        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; panel.add(emailField, gbc);

        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panel.add(passwordField, gbc);

        gbc.gridy = 3; gbc.gridx = 1; panel.add(showPass, gbc);

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(errorLabel, gbc);

        showPass.addActionListener(e -> passwordField.setEchoChar(showPass.isSelected() ? (char) 0 : '*'));

        JButton loginBtn = createButton("Login", new Color(52, 152, 219));
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(loginBtn, gbc);

        JButton toSignup = createLinkButton("I don't have an account? Sign Up");
        gbc.gridy = 6; panel.add(toSignup, gbc);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (!isValidEmail(email)) {
                errorLabel.setText("Email must be lowercase and valid.");
            } else if (!users.containsKey(email)) {
                errorLabel.setText("No account found. Please sign up.");
            } else if (!users.get(email).equals(password)) {
                errorLabel.setText("Invalid email or password.");
            } else {
                errorLabel.setText(" ");
                dispose(); // Close login window
                // Launch MainMenu with the logged-in user's email
                SwingUtilities.invokeLater(() -> new MainMenu(email));
            }
        });

        toSignup.addActionListener(e -> {
            errorLabel.setText(" ");
            switchToSignup();
        });
        return panel;
    }

    private JPanel createSignupPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel title = new JLabel("SIGN UP");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(33, 47, 61));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        JTextField nameField = new JTextField(18);
        JTextField emailField = new JTextField(18);
        JPasswordField passField = new JPasswordField(18);
        JPasswordField confirmPassField = new JPasswordField(18);
        JCheckBox showPass = new JCheckBox("Show Passwords");
        JCheckBox agreeTerms = new JCheckBox("I agree to Terms");

        // Error message label below inputs
        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);

        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);

        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; panel.add(emailField, gbc);

        gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panel.add(passField, gbc);

        gbc.gridy = 4; gbc.gridx = 0; panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; panel.add(confirmPassField, gbc);

        gbc.gridy = 5; gbc.gridx = 1; panel.add(showPass, gbc);

        showPass.addActionListener(e -> {
            char echo = showPass.isSelected() ? (char) 0 : '*';
            passField.setEchoChar(echo);
            confirmPassField.setEchoChar(echo);
        });

        gbc.gridy = 6; gbc.gridx = 1; panel.add(agreeTerms, gbc);

        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(errorLabel, gbc);

        JButton signupBtn = createButton("Sign Up", new Color(46, 204, 113));
        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(signupBtn, gbc);

        JButton toLogin = createLinkButton("Already have an account? Log In");
        gbc.gridy = 9; panel.add(toLogin, gbc);

        signupBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword());
            String confirmPassword = new String(confirmPassField.getPassword());

            if (!isValidName(name)) {
                errorLabel.setText("Name must be at least 2 letters.");
            } else if (!isValidEmail(email)) {
                errorLabel.setText("Email must be lowercase and valid.");
            } else if (users.containsKey(email)) {
                errorLabel.setText("Email already exists.");
            } else if (!isValidPassword(password)) {
                errorLabel.setText("Password must be 8+ chars with upper/lower/digit/special.");
            } else if (!password.equals(confirmPassword)) {
                errorLabel.setText("Passwords do not match.");
            } else if (!agreeTerms.isSelected()) {
                errorLabel.setText("You must agree to the terms.");
            } else {
                errorLabel.setText(" ");
                users.put(email, password);
                saveUserToDatabase(name, email, password);
                showSuccess("Signup successful. Please login.");
                switchToLogin();
            }
        });

        toLogin.addActionListener(e -> {
            errorLabel.setText(" ");
            switchToLogin();
        });

        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    private JButton createLinkButton(String text) {
        JButton button = new JButton(text);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.GRAY);
        return button;
    }

    private void switchToSignup() {
        cardLayout.show(rightPanel, "signup");
        updateImage("/resources/signup.png");
    }

    private void switchToLogin() {
        cardLayout.show(rightPanel, "login");
        updateImage("/resources/login.png");
    }

    private void updateImage(String path) {
        imagePanel.removeAll();
        imagePanel.add(createImageLabel(path), BorderLayout.CENTER);
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    private Connection connect() {
        try {
            String path = "jdbc:ucanaccess://database/users.accdb";
            return DriverManager.getConnection(path);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    private void loadUsersFromFile() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT Email, Password FROM Users");
            while (rs.next()) {
                users.put(rs.getString("Email"), rs.getString("Password"));
            }
        } catch (SQLException e) {
            System.err.println("Failed to load users from database.");
        }
    }

    private void saveUserToDatabase(String name, String email, String password) {
        String query = "INSERT INTO Users (Name, Email, Password) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to save user to database.");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$");
    }

    private boolean isValidName(String name) {
        return name.length() >= 2 && name.matches("[a-zA-Z ]+");
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$");
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthSystem());
    }
}
