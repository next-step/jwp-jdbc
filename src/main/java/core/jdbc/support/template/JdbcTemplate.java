package core.jdbc.support.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import core.jdbc.ConnectionManager;

public class JdbcTemplate {

	public void update(String sql, Object... parameters) throws SQLException {
		executeUpdateQuery(sql, parameters);
	}

	public void insert(String sql, Object... parameters) throws SQLException {
		executeUpdateQuery(sql, parameters);
	}
	public <T> T selectOne(String sql, RowMapper<T> rm, Object... parameters) throws SQLException {
		return executeSelectOneQuery(sql, rm, parameters);
	}

	public <T> List<T> selectAll(String sql, RowMapper<T> rm) throws SQLException {
		return executeSelectAllQuery(sql, rm);
	}

	private void executeUpdateQuery(String sql, Object... parameters) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);
			setParameters(pstmt, parameters);
			pstmt.execute();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}

			if (con != null) {
				con.close();
			}
		}
	}

	private <T> List<T> executeSelectAllQuery(String sql, RowMapper<T> rm, Object... parameters) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		try {
			con = ConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);

			setParameters(pstmt, parameters);
			rs = pstmt.executeQuery();

			List<T> list = new ArrayList<T>();
			while (rs.next()) {
				list.add(rm.mapRow(rs));
			}

			return list;
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}

			if (con != null) {
				con.close();
			}
		}
	}

	private <T> T executeSelectOneQuery(String sql, RowMapper<T> rm, Object... parameters) throws SQLException {
		List<T> list = executeSelectAllQuery(sql, rm, parameters);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	private void setParameters(PreparedStatement pstmt, Object... parameters) throws SQLException {
		for (int i=0; i<parameters.length; i++) {
			pstmt.setString(i + 1, (String) parameters[i]);
		}
	}
}
