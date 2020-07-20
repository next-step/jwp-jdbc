package core.jdbc;

import org.slf4j.Logger;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class JdbcTemplate {

    private static final Logger logger = getLogger(JdbcTemplate.class);

    public void update(String sql, Object... sqlArgs) {
        PreparedStatementCreator psc = bindArgsToSql(sql, sqlArgs);
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = psc.createPreparedStatement(con)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... sqlArgs) {
        PreparedStatementCreator psc = bindArgsToSql(sql, sqlArgs);
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = psc.createPreparedStatement(con);
             ResultSet rs = pstmt.executeQuery()) {
            T object = null;
            if (rs.next()) {
                object = rowMapper.map(rs);
            }
            return object;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper) {
        PreparedStatementCreator psc = bindArgsToSql(sql);
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = psc.createPreparedStatement(con);
             ResultSet rs = pstmt.executeQuery()) {
            List<T> objects = new ArrayList<>();
            while (rs.next()) {
                objects.add(rowMapper.map(rs));
            }
            return objects;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private PreparedStatementCreator bindArgsToSql(String sql, Object... sqlArgs) {
        return new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql);
                for (int i = 0; i < sqlArgs.length; i++) {
                    pstmt.setString(i + 1, (String) sqlArgs[i]);
                }
                return pstmt;
            }
        };
    }
}
