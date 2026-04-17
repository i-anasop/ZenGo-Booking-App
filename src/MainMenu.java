import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.sql.*;

public class MainMenu extends JFrame {
    private final String loggedInEmail;
    private String loggedInName = "User";

    private JPanel profilePanel;
    private boolean profileVisible = false;
    private JLabel profilePicLabel;
    private BufferedImage profileImage;

    public MainMenu(String email) {
        this.loggedInEmail = email;
        this.loggedInName = fetchUserNameByEmail(email);

        setTitle("ZEN Dashboard");
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icon.png"));
        setIconImage(icon.getImage());
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createTopBar(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        profilePanel = createProfilePanel();
        add(profilePanel, BorderLayout.EAST);

        setVisible(true);
    }

    private String fetchUserNameByEmail(String email) {
        String name = "User";
        try (Connection conn = DriverManager.getConnection("jdbc:ucanaccess://database/users.accdb");
             PreparedStatement stmt = conn.prepareStatement("SELECT name FROM users WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(44, 62, 80));
        topBar.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel title = new JLabel("ZEN | Travel with Ease");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

        JButton menuBtn = new JButton("\u2630");
        menuBtn.setFont(new Font("SansSerif", Font.BOLD, 26));
        menuBtn.setForeground(Color.WHITE);
        menuBtn.setBackground(new Color(44, 62, 80));
        menuBtn.setBorder(null);
        menuBtn.setFocusPainted(false);
        menuBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        menuBtn.setPreferredSize(new Dimension(60, 60));
        menuBtn.addActionListener(e -> toggleProfilePanel());

        topBar.add(title, BorderLayout.WEST);
        topBar.add(menuBtn, BorderLayout.EAST);
        return topBar;
    }

    private JPanel createMainContent() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 30);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        JButton busBtn = createBookingButton("Book Bus Ticket", "/resources/bus.png");
        JButton trainBtn = createBookingButton("Book Train Ticket", "/resources/train.png");
        JButton carBtn = createBookingButton("Book Car Ride", "/resources/car.png");
        JButton bikeBtn = createBookingButton("Book Bike Ride", "/resources/bike.png");

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(busBtn, gbc);
        gbc.gridx = 1;
        panel.add(trainBtn, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(carBtn, gbc);
        gbc.gridx = 1;
        panel.add(bikeBtn, gbc);

        return panel;
    }

    private JButton createBookingButton(String text, String iconPath) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(600, 280));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.DARK_GRAY);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setLayout(new BorderLayout());
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        try {
            java.net.URL imgURL = getClass().getResource(iconPath);
            if (imgURL != null) {
                Image img = ImageIO.read(imgURL);
                ImageIcon icon = new ImageIcon(img.getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                JLabel iconLabel = new JLabel(icon);
                iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
                btn.add(iconLabel, BorderLayout.CENTER);
            } else {
                System.err.println("Couldn't find image resource: " + iconPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.add(textLabel, BorderLayout.SOUTH);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(new Color(230, 240, 255));
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(Color.WHITE);
            }
        });

        // Pass loggedInName and loggedInEmail to BookingManager on button click
        btn.addActionListener(e -> new BookingManager(text, loggedInName, loggedInEmail));

        return btn;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(300, getHeight()));
        panel.setBackground(new Color(236, 240, 241));
        panel.setVisible(false);

        profilePicLabel = new JLabel("U", SwingConstants.CENTER);
        profilePicLabel.setBounds(100, 30, 100, 100);
        profilePicLabel.setOpaque(true);
        profilePicLabel.setBackground(new Color(149, 165, 166));
        profilePicLabel.setForeground(Color.WHITE);
        profilePicLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        makeCircular(profilePicLabel);
        panel.add(profilePicLabel);

        JButton changePicBtn = new JButton("Change Picture");
        changePicBtn.setBounds(80, 140, 140, 35);
        changePicBtn.setBackground(new Color(41, 128, 185));
        changePicBtn.setForeground(Color.WHITE);
        changePicBtn.setFocusPainted(false);
        changePicBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        changePicBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        changePicBtn.addActionListener(e -> selectProfilePicture());
        panel.add(changePicBtn);

        JLabel nameLabel = new JLabel("Name: " + loggedInName);
        nameLabel.setBounds(30, 190, 240, 30);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(nameLabel);

        JLabel emailLabel = new JLabel("Email: " + loggedInEmail);
        emailLabel.setBounds(30, 220, 240, 30);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(emailLabel);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(80, 280, 140, 40);
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoutBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(AuthSystem::new);
        });
        panel.add(logoutBtn);

        return panel;
    }

    private void makeCircular(JLabel label) {
        label.setUI(new javax.swing.plaf.basic.BasicLabelUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape circle = new Ellipse2D.Float(0, 0, c.getWidth(), c.getHeight());
                g2.setClip(circle);

                if (profileImage != null) {
                    g2.drawImage(profileImage.getScaledInstance(c.getWidth(), c.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
                } else {
                    super.paint(g2, c);
                }
                g2.dispose();
            }
        });
        label.repaint();
    }

    private void selectProfilePicture() {
        FileDialog fileDialog = new FileDialog(this, "Select Profile Picture", FileDialog.LOAD);
        fileDialog.setVisible(true);

        String directory = fileDialog.getDirectory();
        String file = fileDialog.getFile();

        if (directory != null && file != null) {
            File selectedFile = new File(directory, file);
            String fileName = selectedFile.getName().toLowerCase();
            if (!(fileName.endsWith(".png") || fileName.endsWith(".jpg") ||
                    fileName.endsWith(".jpeg") || fileName.endsWith(".gif"))) {
                JOptionPane.showMessageDialog(this, "Please select a valid image file.", "Invalid File", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                BufferedImage img = ImageIO.read(selectedFile);
                if (img != null) {
                    profileImage = img;
                    profilePicLabel.setText("");
                    profilePicLabel.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to load image.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void toggleProfilePanel() {
        profileVisible = !profileVisible;
        profilePanel.setVisible(profileVisible);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu("user@example.com"));
    }
}
