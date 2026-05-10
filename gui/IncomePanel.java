package Finance.gui;

import Finance.Query.IncomeQuery;
import Finance.EntityFiles.Income;
import Finance.session.UserSession;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class IncomePanel extends JPanel {

    private final MainFrame frame;
    private final IncomeQuery incomeQuery = new IncomeQuery();

    private final JTextField amountField  = new JTextField(15);
    private final JTextField sourceField  = new JTextField(15);
    private final JTextField paymentField = new JTextField(15);
    private final DefaultTableModel tableEntityFiles;

    public IncomePanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 255, 245));

        add(makeHeader("Add Income", new Color(40, 140, 40)), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("New Income"));
        form.setBackground(Color.WHITE);

        form.add(new JLabel("Amount ($):")); form.add(amountField);
        form.add(new JLabel("Source:")); form.add(sourceField);
        form.add(new JLabel("Payment Method:")); form.add(paymentField);

        JButton saveBtn = new JButton("Save Income");
        styleButton(saveBtn, new Color(40, 140, 40));
        form.add(new JLabel()); form.add(saveBtn);

        String[] cols = {"Date", "Amount", "Source", "Payment Method"};
        tableEntityFiles = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableEntityFiles);
        table.setRowHeight(24);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Income History"));

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
            String source = sourceField.getText().trim();
            String pay    = paymentField.getText().trim();

            if (source.isEmpty()) { JOptionPane.showMessageDialog(this, "Source is required."); return; }

            Income inc = new Income(0, LocalDate.now(), amount, source, pay, UserSession.getCurrentUserId());
            incomeQuery.insert(inc);
            JOptionPane.showMessageDialog(this, "Income saved!", "Success", JOptionPane.INFORMATION_MESSAGE);

            amountField.setText(""); sourceField.setText(""); paymentField.setText("");
            loadTable();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTable() {
        if (!UserSession.isLoggedIn()) return;
        tableEntityFiles.setRowCount(0);
        List<Income> list = incomeQuery.getAll(UserSession.getCurrentUserId());
        for (Income i : list) {
            tableEntityFiles.addRow(new Object[]{
                i.getDate(), String.format("$%.2f", i.getAmount()),
                i.getSource(), i.getPaymentMethod()
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
