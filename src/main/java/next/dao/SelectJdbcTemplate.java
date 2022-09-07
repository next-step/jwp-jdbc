package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SelectJdbcTemplate {

    public List query(String sql) {
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            return (List) mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Object queryForObject(String sql) throws SQLException {
        ResultSet rs = null;
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)) {

            setValues(pstmt);
            rs = pstmt.executeQuery();

            return mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    abstract void setValues(PreparedStatement pstmt) throws SQLException;

    abstract Object mapRow(ResultSet rs) throws SQLException;
}
