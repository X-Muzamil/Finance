package Finance.Query;

import Finance.Abstract.BaseQuery;
import Finance.EntityFiles.Expense;
import Finance.interfaces.Insertable;
import Finance.interfaces.Reportable;
import Finance.interfaces.Retrievable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ExpenseQuery extends BaseQuery<Expense>
        implements Insertable<Expense>, Retrievable<Expense>, Reportable {

    @Override protected String getTableName()  { return "Expense"; }
    @Override protected String getPrimaryKey() { return "ExpenseID"; }

    @Override
    public void insert(Expense expense) {
        String sql = "INSERT INTO Expense (Date, Amount, Category, PaymentMethod, Description, UserID) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(expense.getDate()));
            ps.setDouble(2, expense.getAmount());
            ps.setString(3, expense.getCategory());
            ps.setString(4, expense.getPaymentMethod());
            ps.setString(5, expense.getNote());
            ps.setInt(6, expense.getUserId());
            ps.executeUpdate();
            System.out.println("[ExpenseQuery] Expense inserted: $" + expense.getAmount());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Expense> getAll(int userId) {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT * FROM Expense WHERE UserID = ? ORDER BY Date DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Expense getById(int id) {
        String sql = "SELECT * FROM Expense WHERE ExpenseID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Expense> getByCategory(int userId, String category) {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT * FROM Expense WHERE UserID = ? AND Category = ? ORDER BY Date DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, category);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public double getTotalAmount(int userId) {
        String sql = "SELECT SUM(Amount) AS total FROM Expense WHERE UserID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("total");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public String generateSummary(int userId) {
        return String.format("Total Expenses for User %d: $%.2f", userId, getTotalAmount(userId));
    }

    private Expense mapRow(ResultSet rs) throws SQLException {
        return new Expense(
            rs.getInt("ExpenseID"),
            rs.getDate("Date").toLocalDate(),
            rs.getDouble("Amount"),
            rs.getString("Category"),
            rs.getString("PaymentMethod"),
            rs.getString("Description"),
            rs.getInt("UserID")
        );
    }
}
