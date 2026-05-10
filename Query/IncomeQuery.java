package Finance.Query;

import Finance.Abstract.BaseQuery;
import Finance.interfaces.Insertable;
import Finance.interfaces.Retrievable;
import Finance.interfaces.Reportable;
import Finance.EntityFiles.Income;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class IncomeQuery extends BaseQuery<Income>
        implements Insertable<Income>, Retrievable<Income>, Reportable {

    @Override protected String getTableName()  { return "Income"; }
    @Override protected String getPrimaryKey() { return "IncomeID"; }

    @Override
    public void insert(Income income) {
        String sql = "INSERT INTO Income (Date, Amount, Source, PaymentMethod, UserID) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(income.getDate()));
            ps.setDouble(2, income.getAmount());
            ps.setString(3, income.getSource());
            ps.setString(4, income.getPaymentMethod());
            ps.setInt(5, income.getUserId());
            ps.executeUpdate();
            System.out.println("[IncomeQuery] Income inserted: $" + income.getAmount());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Income> getAll(int userId) {
        List<Income> list = new ArrayList<>();
        String sql = "SELECT * FROM Income WHERE UserID = ? ORDER BY Date DESC";
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
    public Income getById(int id) {
        String sql = "SELECT * FROM Income WHERE IncomeID = ?";
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

    
    public double getNetBalance(int userId) {
        String sql = "SELECT " +
                     "(SELECT COALESCE(SUM(Amount),0) FROM Income WHERE UserID = ?) - " +
                     "(SELECT COALESCE(SUM(Amount),0) FROM Expense WHERE UserID = ?) AS balance";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("balance");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public double getTotalAmount(int userId) {
        String sql = "SELECT SUM(Amount) AS total FROM Income WHERE UserID = ?";
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
        return String.format("Total Income for User %d: $%.2f", userId, getTotalAmount(userId));
    }

    private Income mapRow(ResultSet rs) throws SQLException {
        return new Income(
            rs.getInt("IncomeID"),
            rs.getDate("Date").toLocalDate(),
            rs.getDouble("Amount"),
            rs.getString("Source"),
            rs.getString("PaymentMethod"),
            rs.getInt("UserID")
        );
    }
}
