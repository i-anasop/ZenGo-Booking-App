import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class BookingManager extends JFrame {
    private String bookingType;
    private String userName;
    private String userEmail;

    private JTextField dateField, timeField, passengersField;
    private JTextField fromField, toField;
    private JComboBox<String> vehicleTypeCombo;
    private JButton confirmBtn, resetBtn;
    private JTextArea statusArea;

    public BookingManager(String bookingType, String userName, String userEmail) {
        this.bookingType = bookingType;
        this.userName = userName;
        this.userEmail = userEmail;

        setTitle("Booking - " + bookingType);
        setSize(620, 550);
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icon.png"));
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        setResizable(false);

        add(createUserInfoPanel(), BorderLayout.NORTH);
        add(createBookingFormPanel(), BorderLayout.CENTER);
        add(createStatusPanel(), BorderLayout.SOUTH);

        initConfirmButtonState();
        setVisible(true);
    }

    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
        JLabel nameLabel = new JLabel("Name: " + userName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel emailLabel = new JLabel("Email: " + userEmail);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(nameLabel);
        panel.add(emailLabel);
        return panel;
    }

    private JPanel createBookingFormPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
                "Booking Details", 2, 0,
                new Font("Segoe UI", Font.BOLD, 18),
                new Color(41, 128, 185)));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(1, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Booking Type
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel bookingTypeLabel = new JLabel("Booking Type:");
        bookingTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        panel.add(bookingTypeLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        JLabel bookingTypeValueLabel = new JLabel(bookingType);
        bookingTypeValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(bookingTypeValueLabel, gbc);

        // From/Current Location
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel fromLabel = new JLabel(bookingType.toLowerCase().contains("ride") ? "Current Location:" : "From:");
        fromLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(fromLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        fromField = new JTextField();
        addPlaceholder(fromField, "e.g. City Center");
        fromField.setToolTipText("Enter starting point");
        panel.add(fromField, gbc);

        // To/Destination
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        JLabel toLabel = new JLabel(bookingType.toLowerCase().contains("ride") ? "Destination:" : "To:");
        toLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(toLabel, gbc);

        gbc.gridx = 1;
        toField = new JTextField();
        addPlaceholder(toField, "e.g. Airport");
        toField.setToolTipText("Enter destination");
        panel.add(toField, gbc);

        // Date
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(dateLabel, gbc);

        gbc.gridx = 1;
        dateField = new JTextField();
        addPlaceholder(dateField, "e.g. 2025-06-01");
        dateField.setToolTipText("Enter date in format YYYY-MM-DD");
        panel.add(dateField, gbc);

        // Time
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel timeLabel = new JLabel("Time (HH:MM AM/PM):");
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(timeLabel, gbc);

        gbc.gridx = 1;
        timeField = new JTextField();
        addPlaceholder(timeField, "e.g. 02:30 PM");
        timeField.setToolTipText("Enter time in 12-hour format with AM/PM");
        panel.add(timeField, gbc);

        // Passengers
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel passengersLabel = new JLabel("No. of Seats:");
        passengersLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(passengersLabel, gbc);

        gbc.gridx = 1;
        passengersField = new JTextField();
        addPlaceholder(passengersField, "e.g. 1, 2, 3...");
        passengersField.setToolTipText("Enter number of seats required");
        panel.add(passengersField, gbc);

        // Vehicle Type (for rides only)
        if (bookingType.toLowerCase().contains("ride")) {
            gbc.gridx = 0; gbc.gridy = 6;
            JLabel vehicleLabel = new JLabel("Vehicle Type:");
            vehicleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            panel.add(vehicleLabel, gbc);

            gbc.gridx = 1;
            vehicleTypeCombo = new JComboBox<>(new String[]{"Ride Mini", "Ride", "Comfort", "AC"});
            vehicleTypeCombo.setToolTipText("Select vehicle type");
            panel.add(vehicleTypeCombo, gbc);
        } else {
            vehicleTypeCombo = null;
        }

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        confirmBtn = new JButton("Confirm Booking");
        styleButton(confirmBtn, new Color(41, 128, 185), Color.WHITE);
        confirmBtn.addActionListener(e -> handleBookingConfirm());

        resetBtn = new JButton("Reset");
        styleButton(resetBtn, new Color(192, 57, 43), Color.WHITE);
        resetBtn.addActionListener(e -> resetForm());

        btnPanel.add(confirmBtn);
        btnPanel.add(resetBtn);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        addValidationListeners();

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel();
        statusArea = new JTextArea(3, 50);
        statusArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);
        statusArea.setOpaque(false);
        panel.add(statusArea);
        return panel;
    }

    private void addPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void addValidationListeners() {
        DocumentListener listener = new DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validateForm(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validateForm(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validateForm(); }
        };

        dateField.getDocument().addDocumentListener(listener);
        timeField.getDocument().addDocumentListener(listener);
        passengersField.getDocument().addDocumentListener(listener);
        fromField.getDocument().addDocumentListener(listener);
        toField.getDocument().addDocumentListener(listener);

        if (vehicleTypeCombo != null)
            vehicleTypeCombo.addActionListener(e -> validateForm());
    }

    private void initConfirmButtonState() {
        confirmBtn.setEnabled(false);
    }

    private void validateForm() {
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();
        String passengers = passengersField.getText().trim();
        String from = fromField.getText().trim();
        String to = toField.getText().trim();

        boolean valid = true;

        if (date.isEmpty() || date.equals("e.g. 2025-06-01")) valid = false;
        if (time.isEmpty() || time.equals("e.g. 02:30 PM")) valid = false;
        if (passengers.isEmpty() || passengers.equals("e.g. 1, 2, 3...")) valid = false;
        if (from.isEmpty() || from.equals("e.g. City Center")) valid = false;
        if (to.isEmpty() || to.equals("e.g. Airport")) valid = false;

        if (!date.matches("^\\d{4}-\\d{2}-\\d{2}$")) valid = false;
        if (!time.toUpperCase().matches("^([0-1]?[0-9]):[0-5][0-9] ?(AM|PM)$")) valid = false;

        try {
            int p = Integer.parseInt(passengers);
            if (p <= 0) valid = false;
        } catch (NumberFormatException e) {
            valid = false;
        }

        confirmBtn.setEnabled(valid);
    }

    private void handleBookingConfirm() {
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();
        String passengers = passengersField.getText().trim();
        String from = fromField.getText().trim();
        String to = toField.getText().trim();
        String vehicleType = vehicleTypeCombo != null ? vehicleTypeCombo.getSelectedItem().toString() : null;

        if (bookingType.toLowerCase().contains("ride")) {
            new Ticket(userName, bookingType, from, to, date, time, passengers, vehicleType);
        } else {
            new Ticket(userName, bookingType, from, to,date, time, passengers );
        }

        statusArea.setText("Booking confirmed!");
        resetForm();
    }

    private void resetForm() {
        dateField.setText("e.g. 2025-06-01");
        dateField.setForeground(Color.GRAY);

        timeField.setText("e.g. 02:30 PM");
        timeField.setForeground(Color.GRAY);

        passengersField.setText("e.g. 1, 2, 3...");
        passengersField.setForeground(Color.GRAY);

        fromField.setText("e.g. City Center");
        fromField.setForeground(Color.GRAY);

        toField.setText("e.g. Airport");
        toField.setForeground(Color.GRAY);

        if (vehicleTypeCombo != null) {
            vehicleTypeCombo.setSelectedIndex(0);
        }

        statusArea.setText("");
        confirmBtn.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new BookingManager("Ride", "John Doe", "john@example.com"));
    }
}
