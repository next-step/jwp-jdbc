package core.jdbc.support.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import core.jdbc.ConnectionManager;
import core.jdbc.support.exception.DataAccessException;

public class JdbcTemplate {

	private JdbcTemplate() {
	}

	public static JdbcTemplate getInstance() {
		return new JdbcTemplate();
	}

	public void update(String sql, Object... parameters) throws DataAccessException {
		executeUpdateQuery(sql, parameters);
	}

	public void insert(String sql, Object... parameters) throws DataAccessException {
		executeUpdateQuery(sql, parameters);
	}
	public <T> T selectOne(String sql, RowMapper<T> rm, Object... parameters) throws DataAccessException {
		return executeSelectOneQuery(sql, rm, parameters);
	}

	public <T> List<T> selectAll(String sql, RowMapper<T> rm) throws DataAccessException {
		return executeSelectAllQuery(sql, rm);
	}

	private void executeUpdateQuery(String sql, Object... parameters) throws DataAccessException {
		try (Connection con = ConnectionManager.getConnection();
			 PreparedStatement pstmt = con.prepareStatement(sql)
		) {
			setParameters(pstmt, parameters);
			pstmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}

	private <T> List<T> executeSelectAllQuery(String sql, RowMapper<T> rm, Object... parameters) throws DataAccessException {
		try (Connection con = ConnectionManager.getConnection();
			 PreparedStatement pstmt = con.prepareStatement(sql)
		) {
			setParameters(pstmt, parameters);
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

	private <T> T executeSelectOneQuery(String sql, RowMapper<T> rm, Object... parameters) throws DataAccessException {
		List<T> list = executeSelectAllQuery(sql, rm, parameters);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	private void setParameters(PreparedStatement pstmt, Object... parameters) {
		try {
			for (int i = 0; i < parameters.length; i++) {
				pstmt.setString(i + 1, (String) parameters[i]);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
}
