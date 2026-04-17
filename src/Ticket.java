import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Ticket extends JFrame {

    // Constructor for ticket bookings (bus/train)
    public Ticket(String userName, String bookingType, String from, String to,String date, String time, String passengers) {
        String imagePath = "/resources/ticket2.png";  // ticket image
        initUI(userName, bookingType, from, to, date, time, passengers, null, imagePath);
    }

    // Constructor for ride bookings (car/bike) with vehicle type
    public Ticket(String userName, String bookingType, String from, String to, String date, String time, String passengers, String vehicleType) {
        String imagePath = "/resources/ride.png";  // ride image
        initUI(userName, bookingType, from, to, date, time, passengers, vehicleType, imagePath);
    }

    // Shared UI setup method with image path parameter
    private void initUI(String userName, String bookingType, String from, String to, String date, String time, String passengers, String vehicleType, String imagePath) {
        setUndecorated(true);
        setSize(500, vehicleType == null ? 360 : 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(30, 30, 30));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        mainPanel.setLayout(new BorderLayout());

        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        imageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(imageLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(30, 30, 30));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));

        JLabel titleLabel = new JLabel("Booking Confirmed");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel nameLabel = createInfoLabel("Name: " + userName);
        JLabel typeLabel = createInfoLabel("Booking Type: " + bookingType);
        JLabel fromLabel = createInfoLabel("From: " + from);
        JLabel toLabel = createInfoLabel("To: " + to);
        JLabel dateLabel = createInfoLabel("Date: " + date);
        JLabel timeLabel = createInfoLabel("Time: " + time);
        JLabel passengersLabel = createInfoLabel("Passengers: " + passengers);

        // Button Panel at bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(30, 30, 30));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton homeBtn = createActionButton("Home");
        JButton exitBtn = createActionButton("Exit");

        buttonPanel.add(homeBtn);
        buttonPanel.add(exitBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Button listeners
        homeBtn.addActionListener(e -> {
            dispose();  // Close ticket window
        });

        exitBtn.addActionListener(e -> {
            System.exit(0);  // Exit application
        });

        // Add info labels
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(nameLabel);
        infoPanel.add(typeLabel);
        infoPanel.add(fromLabel);
        infoPanel.add(toLabel);
        infoPanel.add(dateLabel);
        infoPanel.add(timeLabel);
        infoPanel.add(passengersLabel);

        if (vehicleType != null) {
            JLabel vehicleLabel = createInfoLabel("Vehicle Type: " + vehicleType);
            infoPanel.add(vehicleLabel);
        }

        // Close button (X)
        JButton closeButton = new JButton("×");
        closeButton.setFocusPainted(false);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.RED);
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setBorderPainted(false);
        closeButton.setPreferredSize(new Dimension(40, 40));
        closeButton.addActionListener(e -> dispose());

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topBar.setBackground(new Color(30, 30, 30));
        topBar.add(closeButton);

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);

        addDragSupport(mainPanel);
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(Color.LIGHT_GRAY);
        return label;
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(50, 50, 50));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return btn;
    }

    private void addDragSupport(Component comp) {
        final Point[] clickPoint = {null};
        comp.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                clickPoint[0] = e.getPoint();
            }
        });
        comp.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point location = getLocation();
                int x = location.x + e.getX() - clickPoint[0].x;
                int y = location.y + e.getY() - clickPoint[0].y;
                setLocation(x, y);
            }
        });
    }
}
