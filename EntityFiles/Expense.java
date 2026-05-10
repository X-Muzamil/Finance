package Finance.EntityFiles;

import Finance.Abstract.FinancialEntity;
import java.time.LocalDate;


public class Expense extends FinancialEntity {

    private String category;
    private String description;

    public Expense() {}

    public Expense(int expenseId, LocalDate date, double amount,
                   String category, String paymentMethod,
                   String description, int userId) {
        super(expenseId, date, amount, paymentMethod, userId);
        this.category    = category;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return "Category: " + category + " | Note: " + description + " | Payment: " + paymentMethod;
    }

    // ─── Expense-specific getters/setters ────────────────────────────────────
    public String getCategory()               { return category; }
    public void   setCategory(String category){ this.category = category; }

    public String getNote()                   { return description; }
    public void   setNote(String description) { this.description = description; }

    // Convenience aliases
    public int    getExpenseId()              { return id; }
    public void   setExpenseId(int id)        { this.id = id; }
}
