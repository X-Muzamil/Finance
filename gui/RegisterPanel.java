package Finance.gui;

import Finance.EntityFiles.User;
import Finance.Query.UserQuery;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class RegisterPanel extends JPanel {

    private final MainFrame frame;
    private final UserQuery userQuery = new UserQuery();

    private final JTextField nameField  = new JTextField(22);
    private final JTextField emailField = new JTextField(22);
    private final JPasswordField passField = new JPasswordField(22);

    // Color Palette (matches LoginPanel)
    private final Color backgroundColor = new Color(245, 245, 245); // Soft gray background
    private final Color emeraldGreen    = new Color(80, 200, 120);  // Primary: Register button
    private final Color navyBlue        = new Color(0, 0, 128);     // Secondary: Headings & Back button
    private final Color charcoalGray    = new Color(54, 69, 79);    // Text / Labels
    private final Color mutedSilver     = new Color(192, 192, 192); // Borders
    private final Color white           = Color.WHITE;

    public RegisterPanel(MainFrame frame) {
        this.frame = frame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new GridBagLayout());
        setBackground(backgroundColor);

        // White card panel
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(white);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(mutedSilver, 1, true),
            new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 4, 10);

        // Title
        JLabel title = new JLabel("Create Your Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(navyBlue);  // Deep blue heading
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(title, gbc);

        // Subtitle
        JLabel subtitle = new JLabel("Start your financial journey today", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(charcoalGray);
        gbc.gridy = 1;
        card.add(subtitle, gbc);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(mutedSilver);
        gbc.gridy = 2; gbc.insets = new Insets(0, 10, 10, 10);
        card.add(sep, gbc);

        gbc.gridwidth = 2;

        // Full Name
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nameLabel.setForeground(charcoalGray);
        gbc.gridy = 3; gbc.insets = new Insets(8, 10, 4, 10);
        card.add(nameLabel, gbc);

        styleField(nameField);
        gbc.gridy = 4; gbc.insets = new Insets(0, 10, 10, 10);
        card.add(nameField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 13));
        emailLabel.setForeground(charcoalGray);
        gbc.gridy = 5; gbc.insets = new Insets(8, 10, 4, 10);
        card.add(emailLabel, gbc);

        styleField(emailField);
        gbc.gridy = 6; gbc.insets = new Insets(0, 10, 10, 10);
        card.add(emailField, gbc);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.BOLD, 13));
        passLabel.setForeground(charcoalGray);
        gbc.gridy = 7; gbc.insets = new Insets(8, 10, 4, 10);
        card.add(passLabel, gbc);

        styleField(passField);
        gbc.gridy = 8; gbc.insets = new Insets(0, 10, 10, 10);
        card.add(passField, gbc);

        // Buttons
        gbc.gridwidth = 1;
        gbc.gridy = 9; gbc.insets = new Insets(15, 10, 10, 5);

        JButton registerBtn = new JButton("Register");
        styleButton(registerBtn, emeraldGreen);  // Solid green — positive, inviting
        gbc.gridx = 0;
        card.add(registerBtn, gbc);

        JButton backBtn = new JButton("Back to Login");
        styleButton(backBtn, navyBlue);          // Deep blue — consistent with login
        gbc.gridx = 1; gbc.insets = new Insets(15, 5, 10, 10);
        card.add(backBtn, gbc);

        // Add card to main panel
        add(card);

        // Actions
        registerBtn.addActionListener(e -> handleRegister());
        backBtn.addActionListener(e -> frame.showScreen("Login"));
        passField.addActionListener(e -> handleRegister());
    }

    private void styleField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(mutedSilver, 1, true),
            new EmptyBorder(6, 8, 6, 8)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setForeground(charcoalGray);
        field.setBackground(white);
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(white);
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

    private void handleRegister() {
        String name  = nameField.getText().trim();
        String email = emailField.getText().trim();
        String pass  = new String(passField.getPassword());

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User newUser = new User(0, name, email, pass);
        boolean success = userQuery.register(newUser);

        if (success) {
            JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.showScreen("Login");
        } else {
            JOptionPane.showMessageDialog(this, "Error: Email already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}