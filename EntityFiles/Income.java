package Finance.EntityFiles;

import Finance.Abstract.FinancialEntity;
import java.time.LocalDate;


public class Income extends FinancialEntity {

    private String source;

    public Income() {}

    public Income(int incomeId, LocalDate date, double amount,
                  String source, String paymentMethod, int userId) {
        super(incomeId, date, amount, paymentMethod, userId);
        this.source = source;
    }

    @Override
    public String getDescription() {
        return "Source: " + source + " | Payment: " + paymentMethod;
    }

    // ─── Income-specific getter/setter ───────────────────────────────────────
    public String getSource()             { return source; }
    public void   setSource(String source){ this.source = source; }

    // Convenience alias matching original field names
    public int    getIncomeId()           { return id; }
    public void   setIncomeId(int id)     { this.id = id; }
}
