package Finance.Query;

import Finance.Abstract.BaseQuery;
import Finance.EntityFiles.Budget;
import Finance.interfaces.Insertable;
import Finance.interfaces.Retrievable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetQuery extends BaseQuery<Budget> implements Insertable<Budget>, Retrievable<Budget> {
    @Override protected String getTableName()  { return "Budget"; }
    @Override protected String getPrimaryKey() { return "BudgetID"; }

    @Override
    public void insert(Budget budget) {
        String sql = "INSERT INTO Budget (Category, LimitAmount, StartDate, EndDate, UserID) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, budget.getCategory());
            ps.setDouble(2, budget.getLimitAmount());
            ps.setDate(3, Date.valueOf(budget.getStartDate()));
            ps.setDate(4, Date.valueOf(budget.getEndDate()));
            ps.setInt(5, budget.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Budget> getAll(int userId) {
        List<Budget> list = new ArrayList<>();
        String sql = "SELECT * FROM Budget WHERE UserID = ?";
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
    public Budget getById(int id) {
        String sql = "SELECT * FROM Budget WHERE BudgetID = ?";
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

    public double getRemainingBudget(int userId, String category) {
        String sql = "SELECT b.LimitAmount - COALESCE(SUM(e.Amount), 0) AS remaining " +
                     "FROM Budget b LEFT JOIN Expense e " +
                     "ON b.UserID = e.UserID AND b.Category = e.Category " +
                     "AND e.Date BETWEEN b.StartDate AND b.EndDate " +
                     "WHERE b.UserID = ? AND b.Category = ? " +
                     "GROUP BY b.LimitAmount";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, category);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("remaining");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private Budget mapRow(ResultSet rs) throws SQLException {
        return new Budget(
            rs.getInt("BudgetID"),
            rs.getString("Category"),
            rs.getDouble("LimitAmount"),
            rs.getDate("StartDate").toLocalDate(),
            rs.getDate("EndDate").toLocalDate(),
            rs.getInt("UserID")
        );
    }
}