package Finance.Abstract;

import java.time.LocalDate;


public abstract class FinancialEntity {

    protected int id;
    protected LocalDate date;
    protected double amount;
    protected String paymentMethod;
    protected int userId;

    public FinancialEntity() {}

    public FinancialEntity(int id, LocalDate date, double amount, String paymentMethod, int userId) {
        this.id            = id;
        this.date          = date;
        this.amount        = amount;
        this.paymentMethod = paymentMethod;
        this.userId        = userId;
    }

   
    public abstract String getDescription();

    public int getId()                        { return id; }
    public void setId(int id)                 { this.id = id; }

    public LocalDate getDate()                { return date; }
    public void setDate(LocalDate date)       { this.date = date; }

    public double getAmount()                 { return amount; }
    public void setAmount(double amount)      { this.amount = amount; }

    public String getPaymentMethod()                      { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod)    { this.paymentMethod = paymentMethod; }

    public int getUserId()                    { return userId; }
    public void setUserId(int userId)         { this.userId = userId; }

    @Override
    public String toString() {
        return "[" + getClass().getSimpleName() + "] Date=" + date +
            " | Amount=" + amount + " | " + getDescription();
    }
}
