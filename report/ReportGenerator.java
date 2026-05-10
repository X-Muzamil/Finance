package Finance.report;

import Finance.Query.BudgetQuery;
import Finance.Query.ExpenseQuery;
import Finance.Query.IncomeQuery;
import Finance.Query.ReportQuery;
import Finance.interfaces.Reportable;
import Finance.EntityFiles.*;
import java.time.LocalDate;
import java.util.List;


public class ReportGenerator {

    private final IncomeQuery  incomeQuery  = new IncomeQuery();
    private final ExpenseQuery expenseQuery = new ExpenseQuery();
    private final BudgetQuery  budgetQuery  = new BudgetQuery();
    private final ReportQuery  reportQuery  = new ReportQuery();

    
    public String generateFullReport(int userId) {
        Reportable incomeReport  = incomeQuery;
        Reportable expenseReport = expenseQuery;

        double totalIncome   = incomeReport.getTotalAmount(userId);
        double totalExpenses = expenseReport.getTotalAmount(userId);
        double netBalance    = incomeQuery.getNetBalance(userId);

        StringBuilder sb = new StringBuilder();
        sb.append("====================================\n");
        sb.append("   PERSONAL FINANCE REPORT\n");
        sb.append("   Generated: ").append(LocalDate.now()).append("\n");
        sb.append("====================================\n");
        sb.append(incomeReport.generateSummary(userId)).append("\n");
        sb.append(expenseReport.generateSummary(userId)).append("\n");
        sb.append(String.format("Net Balance          : $%.2f%n", netBalance));
        sb.append("====================================\n");

        List<Budget> budgets = budgetQuery.getAll(userId);
        if (!budgets.isEmpty()) {
            sb.append("\nBUDGET STATUS:\n");
            for (Budget b : budgets) {
                double remaining = budgetQuery.getRemainingBudget(userId, b.getCategory());
                String status    = remaining >= 0 ? "OK" : "OVER BUDGET";
                sb.append(String.format("  %-15s Limit: $%.2f  Remaining: $%.2f  [%s]%n",
                          b.getCategory(), b.getLimitAmount(), remaining, status));
            }
        }

        String report = sb.toString();
        System.out.println(report);

        Report meta = new Report(0, "FULL", LocalDate.now(), "console", userId);
        reportQuery.insert(meta);

        return report;
    }

    
    public String generateIncomeSummary(int userId) {
        return incomeQuery.generateSummary(userId);
    }

   
    public String generateExpenseSummary(int userId) {
        return expenseQuery.generateSummary(userId);
    }
}
