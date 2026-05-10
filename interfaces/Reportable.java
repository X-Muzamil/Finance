package Finance.interfaces;

public interface Reportable {
    double getTotalAmount(int userId);
    String generateSummary(int userId);
}
