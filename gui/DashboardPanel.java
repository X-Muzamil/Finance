package Finance.gui;

import Finance.Query.ExpenseQuery;
import Finance.Query.IncomeQuery;
import Finance.session.UserSession;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class DashboardPanel extends JPanel {

    private final MainFrame frame;
    private final IncomeQuery incomeQuery = new IncomeQuery();
    private final ExpenseQuery expenseQuery = new ExpenseQuery();

    private JLabel incomeLabel  = new JLabel();
    private JLabel expenseLabel = new JLabel();
    private JLabel balanceLabel = new JLabel();
    private JLabel welcomeLabel = new JLabel();

    // Color Palette (matches Login & Register)
    private final Color backgroundColor = new Color(245, 245, 245);  // Soft gray background
    private final Color navyBlue        = new Color(0, 0, 128);      // Header & Budget button
    private final Color emeraldGreen    = new Color(80, 200, 120);   // Income card & button
    private final Color crimsonRed      = new Color(200, 50, 50);    // Expense card & button
    private final Color charcoalGray    = new Color(54, 69, 79);     // Labels
    private final Color mutedSilver     = new Color(192, 192, 192);  // Borders
    private final Color white           = Color.WHITE;

    public DashboardPanel(MainFrame frame) {
        this.frame = frame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(backgroundColor);

        // ── Header Bar ──────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(navyBlue);   // Deep navy blue — trust/security
        topBar.setBorder(new EmptyBorder(14, 24, 14, 24));

        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(white);
        topBar.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        styleButton(logoutBtn, crimsonRed);
        logoutBtn.addActionListener(e -> {
            UserSession.logout();
            frame.showScreen("Login");
        });
        topBar.add(logoutBtn, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // ── Summary Cards ────────────────────────────────────────────
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        summaryPanel.setBackground(backgroundColor);
        summaryPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        // Green card — Income (positive/growth)
        summaryPanel.add(makeCard("Total Income",   incomeLabel,  emeraldGreen));
        // Red card — Expenses (negative/spending)
        summaryPanel.add(makeCard("Total Expenses", expenseLabel, crimsonRed));
        // Blue card — Balance (trust/neutral container, value color set dynamically)
        summaryPanel.add(makeCard("Net Balance",    balanceLabel, navyBlue));

        add(summaryPanel, BorderLayout.CENTER);

        // ── Navigation Buttons ───────────────────────────────────────
        JPanel navPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        navPanel.setBackground(backgroundColor);
        navPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        // Red  — negative action (spending)
        navPanel.add(makeNavButton("Add Expense",  "Expenses", crimsonRed));
        // Green — positive action (earning)
        navPanel.add(makeNavButton("Add Income",   "Income",   emeraldGreen));
        // Navy blue — neutral planning action
        navPanel.add(makeNavButton("Set Budget",   "Budget",   navyBlue));
        // Navy blue outline — secondary but important
        navPanel.add(makeNavButtonOutline("View Reports", "Report"));

        add(navPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        if (!UserSession.isLoggedIn()) return;

        int userId = UserSession.getCurrentUserId();
        welcomeLabel.setText("Welcome, " + UserSession.getCurrentUser().getUsername() + "!");

        double income   = incomeQuery.getTotalAmount(userId);
        double expenses = expenseQuery.getTotalAmount(userId);
        double balance  = income - expenses;

        SwingUtilities.invokeLater(() -> {
            incomeLabel.setText(String.format("$%.2f", income));
            expenseLabel.setText(String.format("$%.2f", expenses));
            balanceLabel.setText(String.format("$%.2f", balance));
            // Green if positive, red if negative
            balanceLabel.setForeground(balance >= 0 ? emeraldGreen : crimsonRed);
        });
    }

    // ── Card Builder ─────────────────────────────────────────────────
    private JPanel makeCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(white);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(accent, 2, true),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(charcoalGray);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(accent);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setText("$0.00");

        card.add(titleLabel);
        card.add(valueLabel);
        return card;
    }

    // ── Solid Nav Button ─────────────────────────────────────────────
    private JButton makeNavButton(String text, String screen, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(white);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(160, 45));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> frame.showScreen(screen));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited (java.awt.event.MouseEvent e) { btn.setBackground(color); }
        });
        return btn;
    }

    // ── Outline Nav Button (View Reports) ────────────────────────────
    private JButton makeNavButtonOutline(String text, String screen) {
        JButton btn = new JButton(text);
        btn.setBackground(white);
        btn.setForeground(navyBlue);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(navyBlue, 2, true));
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(160, 45));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> frame.showScreen(screen));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(navyBlue);
                btn.setForeground(white);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(white);
                btn.setForeground(navyBlue);
            }
        });
        return btn;
    }

    // ── Generic Button Styler ────────────────────────────────────────
    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(white);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(100, 34));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(bgColor.darker()); }
            public void mouseExited (java.awt.event.MouseEvent e) { btn.setBackground(bgColor); }
        });
    }
}