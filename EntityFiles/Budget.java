package Finance.EntityFiles;

import java.time.LocalDate;


public class Budget {

    private int       budgetId;
    private String    category;
    private double    limitAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private int       userId;

    public Budget() {}

    public Budget(int budgetId, String category, double limitAmount,
                LocalDate startDate, LocalDate endDate, int userId) {
        this.budgetId    = budgetId;
        this.category    = category;
        this.limitAmount = limitAmount;
        this.startDate   = startDate;
        this.endDate     = endDate;
        this.userId      = userId;
    }

    public int       getBudgetId()                       { return budgetId; }
    public void      setBudgetId(int budgetId)           { this.budgetId = budgetId; }

    public String    getCategory()                       { return category; }
    public void      setCategory(String category)        { this.category = category; }

    public double    getLimitAmount()                    { return limitAmount; }
    public void      setLimitAmount(double limitAmount)  { this.limitAmount = limitAmount; }

    public LocalDate getStartDate()                      { return startDate; }
    public void      setStartDate(LocalDate startDate)   { this.startDate = startDate; }

    public LocalDate getEndDate()                        { return endDate; }
    public void      setEndDate(LocalDate endDate)       { this.endDate = endDate; }

    public int       getUserId()                         { return userId; }
    public void      setUserId(int userId)               { this.userId = userId; }

    @Override
    public String toString() {
        return "Budget{category='" + category + "', limit=" + limitAmount +
         ", from=" + startDate + ", to=" + endDate + "}";
    }
}
