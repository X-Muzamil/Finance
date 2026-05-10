package Finance.Abstract;

import Finance.db.DBConnection;
import Finance.interfaces.Deletable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class BaseQuery<T> implements Deletable {

    protected abstract String getTableName();
    protected abstract String getPrimaryKey();


    @Override
    public boolean delete(int id) {
        if (isUnsafe(getTableName()) || isUnsafe(getPrimaryKey())) {
            System.err.println("[BaseQuery] Potential SQL Injection detected in table structure!");
            return false;
        }

        String sql = "DELETE FROM " + getTableName() + " WHERE " + getPrimaryKey() + " = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("[BaseQuery] Error deleting record: " + e.getMessage());
            return false;
        }
    }

    
    private boolean isUnsafe(String input) {
        if (input == null || input.isEmpty()) return true;
        return !input.matches("^[a-zA-Z0-0_]+$");
    }

    protected ResultSet executeQuery(PreparedStatement ps) throws SQLException {
        return ps.executeQuery();
    }

    protected Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }
}