package Finance.gui;

import Finance.report.ReportGenerator;
import Finance.session.UserSession;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ReportPanel extends JPanel {

    private final MainFrame       frame;
    private final ReportGenerator generator  = new ReportGenerator();
    private final JTextArea       reportArea = new JTextArea();

    // Color Palette (matches Login, Register, Dashboard)
    private final Color backgroundColor = new Color(245, 245, 245); // Soft gray background
    private final Color navyBlue        = new Color(0, 0, 128);     // Header & Full Report button
    private final Color emeraldGreen    = new Color(80, 200, 120);  // Income Summary button
    private final Color crimsonRed      = new Color(200, 50, 50);   // Expense Summary button
    private final Color charcoalGray    = new Color(54, 69, 79);    // Text
    private final Color mutedSilver     = new Color(192, 192, 192); // Borders
    private final Color white           = Color.WHITE;

    public ReportPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(backgroundColor);

        // ── Header ───────────────────────────────────────────────────
        add(makeHeader(), BorderLayout.NORTH);

        // ── Report Output Area ───────────────────────────────────────
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setEditable(false);
        reportArea.setBackground(white);
        reportArea.setForeground(charcoalGray);
        reportArea.setMargin(new Insets(12, 16, 12, 16));
        reportArea.setText("Click a button below to generate a report.");

        JScrollPane scroll = new JScrollPane(reportArea);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(10, 20, 5, 20),
            BorderFactory.createCompoundBorder(
                new LineBorder(mutedSilver, 1, true),
                new EmptyBorder(5, 5, 5, 5)
            )
        ));
        add(scroll, BorderLayout.CENTER);

        // ── Buttons ──────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        btnPanel.setBackground(backgroundColor);
        btnPanel.setBorder(new EmptyBorder(0, 20, 10, 20));

        JButton fullReport    = makeNavButton("Full Report",       navyBlue);    // Primary — navy blue solid
        JButton incomeReport  = makeNavButton("Income Summary",    emeraldGreen); // Positive — green solid
        JButton expenseReport = makeNavButton("Expense Summary",   crimsonRed);   // Cautionary — red solid
        JButton backBtn       = makeOutlineButton("← Back");                      // Secondary — navy outline

        btnPanel.add(fullReport);
        btnPanel.add(incomeReport);
        btnPanel.add(expenseReport);
        btnPanel.add(backBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // ── Actions ──────────────────────────────────────────────────
        fullReport.addActionListener(e -> {
            int uid = UserSession.getCurrentUserId();
            reportArea.setText(generator.generateFullReport(uid));
        });
        incomeReport.addActionListener(e -> {
            int uid = UserSession.getCurrentUserId();
            reportArea.setText(generator.generateIncomeSummary(uid));
        });
        expenseReport.addActionListener(e -> {
            int uid = UserSession.getCurrentUserId();
            reportArea.setText(generator.generateExpenseSummary(uid));
        });
        backBtn.addActionListener(e -> frame.showScreen("Dashboard"));
    }

    // ── Header Builder ───────────────────────────────────────────────
    private JPanel makeHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(navyBlue);  // Deep navy — matches Login & Dashboard headers
        p.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel title = new JLabel("Financial Reports");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(white);
        p.add(title, BorderLayout.WEST);

        return p;
    }

    // ── Solid Button ─────────────────────────────────────────────────
    private JButton makeNavButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(white);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(165, 42));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited (java.awt.event.MouseEvent e) { btn.setBackground(color); }
        });
        return btn;
    }

    // ── Outline Button (Back) ────────────────────────────────────────
    private JButton makeOutlineButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(white);
        btn.setForeground(navyBlue);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(navyBlue, 2, true));
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(165, 42));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
}