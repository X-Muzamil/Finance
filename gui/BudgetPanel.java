package Finance.gui;

import Finance.Query.BudgetQuery;
import Finance.EntityFiles.Budget;
import Finance.session.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class BudgetPanel extends JPanel {

    private final MainFrame frame;
    private final BudgetQuery budgetQuery = new BudgetQuery();

    private final JTextField categoryField = new JTextField(15);
    private final JTextField limitField    = new JTextField(15);
    private final DefaultTableModel tableEntityFiles;

    public BudgetPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(250, 245, 255));

        add(makeHeader("Budget Planner", new Color(140, 80, 200)), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Set Budget Limit"));
        form.setBackground(Color.WHITE);

        form.add(new JLabel("Category:")); form.add(categoryField);
        form.add(new JLabel("Limit Amount ($):")); form.add(limitField);

        JButton saveBtn = new JButton("Set Budget");
        styleButton(saveBtn, new Color(140, 80, 200));
        form.add(new JLabel()); form.add(saveBtn);

        String[] cols = {"Category", "Limit ($)", "Remaining ($)", "Status"};
        tableEntityFiles = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableEntityFiles);
        table.setRowHeight(26);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Budget Status"));

        JPanel center = new JPanel(new BorderLayout(5, 5));
        center.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        center.add(form, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener(e -> frame.showScreen("Dashboard"));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(backBtn);
        add(bottom, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> handleSave());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        loadTable();
    }

    private void handleSave() {
        try {
            String cat   = categoryField.getText().trim();
            double limit = Double.parseDouble(limitField.getText().trim());

            if (cat.isEmpty()) { JOptionPane.showMessageDialog(this, "Category is required."); return; }

            Budget budget = new Budget(0, cat, limit,
                LocalDate.now(), LocalDate.now().plusMonths(1),
                UserSession.getCurrentUserId());
            budgetQuery.insert(budget);
            JOptionPane.showMessageDialog(this, "Budget set for: " + cat, "Success", JOptionPane.INFORMATION_MESSAGE);

            categoryField.setText(""); limitField.setText("");
            loadTable();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void loadTable() {
        if (!UserSession.isLoggedIn()) return;
        int userId = UserSession.getCurrentUserId();
        tableEntityFiles.setRowCount(0);
        List<Budget> list = budgetQuery.getAll(userId);
        for (Budget b : list) {
            double remaining = budgetQuery.getRemainingBudget(userId, b.getCategory());
            String status    = remaining >= 0 ? "✔ OK" : "✘ Over Budget";
            tableEntityFiles.addRow(new Object[]{
                b.getCategory(),
                String.format("%.2f", b.getLimitAmount()),
                String.format("%.2f", remaining),
                status
            });
        }
    }

    private JPanel makeHeader(String title, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(color);
        JLabel l = new JLabel("  " + title);
        l.setFont(new Font("Arial", Font.BOLD, 18));
        l.setForeground(Color.WHITE);
        p.add(l); return p;
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setFont(new Font("Arial", Font.BOLD, 13));
    }
}
