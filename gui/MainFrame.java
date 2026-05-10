package Finance.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     mainPanel  = new JPanel(cardLayout);

    public MainFrame() {
        setTitle("Personal Finance Manager");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.add(new LoginPanel(this),     "Login");
        mainPanel.add(new RegisterPanel(this),  "Register");
        mainPanel.add(new DashboardPanel(this), "Dashboard");
        mainPanel.add(new ExpensePanel(this),   "Expenses");
        mainPanel.add(new IncomePanel(this),    "Income");
        mainPanel.add(new BudgetPanel(this),    "Budget");
        mainPanel.add(new ReportPanel(this),    "Report");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");  
    }

    
    public void showScreen(String screenName) {
        if (screenName.equals("Dashboard")) {
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof DashboardPanel) {
                    ((DashboardPanel) comp).refreshData();
                }
            }
        }
        
        if (screenName.equals("Budget")) {
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof BudgetPanel) {
                    ((BudgetPanel) comp).loadTable(); 
                }
            }
        }

        cardLayout.show(mainPanel, screenName);
    }
}