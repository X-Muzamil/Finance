package Finance.gui;

import Finance.EntityFiles.User;
import Finance.Query.UserQuery;
import Finance.session.UserSession;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class LoginPanel extends JPanel {

    private final MainFrame frame;
    private final UserQuery userQuery = new UserQuery();

    private final JTextField emailField = new JTextField(22);
    private final JPasswordField passField = new JPasswordField(22);

    // Color Palette
    private final Color backgroundColor = new Color(245, 245, 245); // Slightly warmer light gray
    private final Color navyBlue       = new Color(0, 0, 128);
    private final Color emeraldGreen   = new Color(80, 200, 120);
    private final Color charcoalGray   = new Color(54, 69, 79);
    private final Color mutedSilver    = new Color(192, 192, 192);
    private final Color white          = Color.WHITE;

    public LoginPanel(MainFrame frame) {
        this.frame = frame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new GridBagLayout());
        setBackground(backgroundColor);

        // Card panel for the form
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(white);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(mutedSilver, 1, true),
            new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Personal Finance Manager", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(navyBlue);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(title, gbc);

        // Subtitle
        JLabel subtitle = new JLabel("Login to your account", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitle.setForeground(charcoalGray);
        gbc.gridy = 1;
        card.add(subtitle, gbc);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(mutedSilver);
        gbc.gridy = 2; gbc.insets = new Insets(0, 10, 10, 10);
        card.add(sep, gbc);

        gbc.insets = new Insets(8, 10, 4, 10);
        gbc.gridwidth = 2;

        // Email Label
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 13));
        emailLabel.setForeground(charcoalGray);
        gbc.gridy = 3;
        card.add(emailLabel, gbc);

        // Email Field
        styleField(emailField);
        gbc.gridy = 4; gbc.insets = new Insets(0, 10, 10, 10);
        card.add(emailField, gbc);

        // Password Label
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.BOLD, 13));
        passLabel.setForeground(charcoalGray);
        gbc.gridy = 5; gbc.insets = new Insets(8, 10, 4, 10);
        card.add(passLabel, gbc);

        // Password Field
        styleField(passField);
        gbc.gridy = 6; gbc.insets = new Insets(0, 10, 10, 10);
        card.add(passField, gbc);

        // Buttons
        gbc.gridwidth = 1;
        gbc.gridy = 7; gbc.insets = new Insets(15, 10, 10, 5);

        JButton loginBtn = new JButton("Login");
        styleButton(loginBtn, navyBlue);
        gbc.gridx = 0;
        card.add(loginBtn, gbc);

        JButton registerBtn = new JButton("Create Account");
        styleButton(registerBtn, emeraldGreen);
        gbc.gridx = 1; gbc.insets = new Insets(15, 5, 10, 10);
        card.add(registerBtn, gbc);

        // Add card to main panel
        add(card);

        // Actions
        loginBtn.addActionListener(e -> handleLogin());
        registerBtn.addActionListener(e -> frame.showScreen("Register"));
        passField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userQuery.login(email, password);

        if (user != null) {
            UserSession.login(user);
            frame.showScreen("Dashboard");
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            passField.setText("");
        }
    }

    private void styleField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(mutedSilver, 1, true),
            new EmptyBorder(6, 8, 6, 8)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setForeground(charcoalGray);
        field.setBackground(Color.WHITE);
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(155, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            final Color original = bgColor;
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(original.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(original);
            }
        });
    }
}