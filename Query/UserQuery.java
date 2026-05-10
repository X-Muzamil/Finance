package Finance.Query;

import Finance.Abstract.BaseQuery;
import Finance.interfaces.Insertable;
import Finance.interfaces.Retrievable;
import Finance.EntityFiles.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserQuery extends BaseQuery<User>
        implements Insertable<User>, Retrievable<User> {

    @Override
    protected String getTableName()  { return "Users"; }

    @Override
    protected String getPrimaryKey() { return "UserID"; }

    @Override
    public void insert(User user) {
        if (emailExists(user.getEmail())) {
            System.out.println("[UserQuery] Email already registered: " + user.getEmail());
            return;
        }

        String sql = "INSERT INTO Users (Username, Email, PasswordHash) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.executeUpdate();
            System.out.println("[UserQuery] User registered: " + user.getUsername());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAll(int userId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User getById(int id) {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
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

  
    public User login(String email, String password) {
        String sql = "SELECT * FROM Users WHERE Email = ? AND PasswordHash = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);   
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

   
    public boolean register(User user) {
        if (emailExists(user.getEmail())) return false;
        insert(user);
        return true;
    }


    private boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("UserID"),
            rs.getString("Username"),
            rs.getString("Email"),
            rs.getString("PasswordHash")
        );
    }
}
