package core.jdbc.support.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import core.jdbc.support.exception.DataAccessException;

public class JdbcTemplate {
    private final DataSource dataSource;

    private JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static JdbcTemplate getInstance(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public void update(String sql, Object... parameters) throws DataAccessException {
        executeUpdateQuery(sql, createPreparedStatementSetter(parameters));
    }

    public void insert(String sql, Object... parameters) throws DataAccessException {
        executeUpdateQuery(sql, createPreparedStatementSetter(parameters));
    }

    public void update(String sql, PreparedStatementSetter pss) throws DataAccessException {
        executeUpdateQuery(sql, pss);
    }

    public void insert(String sql, PreparedStatementSetter pss) throws DataAccessException {
        executeUpdateQuery(sql, pss);
    }

    public <T> T selectOne(String sql, RowMapper<T> rm, Object... parameters) throws DataAccessException {
        return selectOne(sql, rm, createPreparedStatementSetter(parameters));
    }

    public <T> T selectOne(String sql, RowMapper<T> rm, PreparedStatementSetter pss) throws DataAccessException {
        List<T> list = executeSelectAllQuery(sql, rm, pss);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public <T> List<T> selectAll(String sql, RowMapper<T> rm) throws DataAccessException {
        return executeSelectAllQuery(sql, rm);
    }

    private void executeUpdateQuery(String sql, PreparedStatementSetter pss) throws DataAccessException {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pss.setParameters(pstmt);
            pstmt.execute();
        } catch(SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private <T> List<T> executeSelectAllQuery(String sql, RowMapper<T> rm, Object... parameters) throws DataAccessException {
        return executeSelectAllQuery(sql, rm, createPreparedStatementSetter(parameters));
    }

    private <T> List<T> executeSelectAllQuery(String sql, RowMapper<T> rm, PreparedStatementSetter pss) throws DataAccessException {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pss.setParameters(pstmt);
            ResultSet rs = pstmt.executeQuery();

            List<T> list = new ArrayList<T>();
            while (rs.next()) {
                list.add(rm.mapRow(rs));
            }

            return list;
        } catch(SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object... parameters) {
        return pstmt -> {
            for (int i = 0; i < parameters.length; i++) {
                pstmt.setString(i + 1, (String) parameters[i]);
            }
        };
    }
}
