package Finance.gui;

import Finance.Query.ExpenseQuery;
import Finance.EntityFiles.Expense;
import Finance.session.UserSession;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class ExpensePanel extends JPanel {

    private final MainFrame  frame;
    private final ExpenseQuery expenseQuery = new ExpenseQuery();

    private final JTextField amountField   = new JTextField(15);
    private final JTextField categoryField = new JTextField(15);
    private final JTextField descField     = new JTextField(15);
    private final JTextField paymentField  = new JTextField(15);
    private final DefaultTableModel tableEntityFiles;

    public ExpensePanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(255, 248, 245));

        JPanel header = makeHeader("Add Expense", new Color(200, 80, 40));
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("New Expense"));
        form.setBackground(Color.WHITE);

        form.add(new JLabel("Amount ($):")); form.add(amountField);
        form.add(new JLabel("Category:")); form.add(categoryField);
        form.add(new JLabel("Payment Method:")); form.add(paymentField);
        form.add(new JLabel("Description:")); form.add(descField);

        JButton saveBtn = new JButton("Save Expense");
        styleButton(saveBtn, new Color(200, 80, 40));
        form.add(new JLabel());
        form.add(saveBtn);

        String[] cols = {"Date", "Amount", "Category", "Payment", "Description"};
        tableEntityFiles = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableEntityFiles);
        table.setRowHeight(24);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Recent Expenses"));

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
            double amount = Double.parseDouble(amountField.getText().trim());
            String cat    = categoryField.getText().trim();
            String pay    = paymentField.getText().trim();
            String desc   = descField.getText().trim();

            if (cat.isEmpty()) { JOptionPane.showMessageDialog(this, "Category is required."); return; }

            Expense exp = new Expense(0, LocalDate.now(), amount, cat, pay, desc,
                                      UserSession.getCurrentUserId());
            expenseQuery.insert(exp);
            JOptionPane.showMessageDialog(this, "Expense saved!", "Success", JOptionPane.INFORMATION_MESSAGE);

            amountField.setText(""); categoryField.setText("");
            paymentField.setText(""); descField.setText("");
            loadTable();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTable() {
        if (!UserSession.isLoggedIn()) return;
        tableEntityFiles.setRowCount(0);
        List<Expense> list = expenseQuery.getAll(UserSession.getCurrentUserId());
        for (Expense e : list) {
            tableEntityFiles.addRow(new Object[]{
                e.getDate(), String.format("$%.2f", e.getAmount()),
                e.getCategory(), e.getPaymentMethod(), e.getNote()
            });
        }
    }

    private JPanel makeHeader(String title, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(color);
        JLabel l = new JLabel("  " + title);
        l.setFont(new Font("Arial", Font.BOLD, 18));
        l.setForeground(Color.WHITE);
        p.add(l);
        return p;
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setFont(new Font("Arial", Font.BOLD, 13));
    }
}
