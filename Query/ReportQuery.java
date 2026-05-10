package Finance.Query;

import Finance.Abstract.BaseQuery;
import Finance.interfaces.Insertable;
import Finance.interfaces.Retrievable;
import Finance.EntityFiles.Report;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ReportQuery extends BaseQuery<Report>
        implements Insertable<Report>, Retrievable<Report> {

    @Override protected String getTableName()  { return "Report"; }
    @Override protected String getPrimaryKey() { return "ReportID"; }

    @Override
    public void insert(Report report) {
        String sql = "INSERT INTO Report (Type, GeneratedDate, FilePath, UserID) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, report.getType());
            ps.setDate(2, Date.valueOf(report.getGeneratedDate()));
            ps.setString(3, report.getFilePath());
            ps.setInt(4, report.getUserId());
            ps.executeUpdate();
            System.out.println("[ReportQuery] Report saved: " + report.getType());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Report> getAll(int userId) {
        List<Report> list = new ArrayList<>();
        String sql = "SELECT * FROM Report WHERE UserID = ? ORDER BY GeneratedDate DESC";
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
    public Report getById(int id) {
        String sql = "SELECT * FROM Report WHERE ReportID = ?";
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

    private Report mapRow(ResultSet rs) throws SQLException {
        return new Report(
            rs.getInt("ReportID"),
            rs.getString("Type"),
            rs.getDate("GeneratedDate").toLocalDate(),
            rs.getString("FilePath"),
            rs.getInt("UserID")
        );
    }
}
