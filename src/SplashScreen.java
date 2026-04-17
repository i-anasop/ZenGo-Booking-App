import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends JFrame {

    private JLabel dotLabel;
    private int dotCount = 0;

    public SplashScreen() {

        setSize(1200, 720);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setLayout(new BorderLayout());

        // Rounded window edges
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));

        // Premium background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel, BorderLayout.CENTER);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 20, 20, 20);

        // === LOGO ===
        URL imgURL = getClass().getResource("/resources/logo.png");
        if (imgURL != null) {
            ImageIcon logoIcon = new ImageIcon(imgURL);
            int logoSize = 350;
            Image scaledImage = logoIcon.getImage().getScaledInstance(logoSize, logoSize, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(scaledImage);

            JLabel logoLabel = new JLabel(logoIcon);
            logoLabel.setOpaque(false);
            gbc.gridy = 0;
            backgroundPanel.add(logoLabel, gbc);
        } else {
            System.err.println("Logo not found!");
        }

        // === LOADING TEXT ===
        dotLabel = new JLabel("Starting GoZen ");
        dotLabel.setFont(new Font("Segoe UI", Font.BOLD , 15));
        dotLabel.setForeground(Color.WHITE);
        dotLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(40, 20, 60, 20);
        backgroundPanel.add(dotLabel, gbc);
        setVisible(true);
        startLoadingAnimation();
        startSplashTimer();
    }

    private void startLoadingAnimation() {
        Timer dotTimer = new Timer();
        dotTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                dotCount = (dotCount + 1) % 4;
                StringBuilder dots = new StringBuilder();
                for (int i = 0; i < dotCount; i++) dots.append(".");
                dotLabel.setText("Starting GoZen " + dots);
            }
        }, 0, 500);
    }

    private void startSplashTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dispose();
                new AuthSystem();
            }
        }, 4000);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SplashScreen::new);
    }

    // === Custom Panel with Solid Brand Background ===
    static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Brand navy background
            g2.setColor(new Color(21, 101, 192)); // #0A0C1C
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
