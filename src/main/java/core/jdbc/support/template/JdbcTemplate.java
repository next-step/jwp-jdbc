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
	public Object selectOne(String sql, RowMapper rm, Object... parameters) throws SQLException {
		return executeSelectOneQuery(sql, rm, parameters);
	}

	public List<Object> selectAll(String sql, RowMapper rm) throws SQLException {
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

	private List<Object> executeSelectAllQuery(String sql, RowMapper rm, Object... parameters) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		try {
			con = ConnectionManager.getConnection();
			pstmt = con.prepareStatement(sql);

			setParameters(pstmt, parameters);
			rs = pstmt.executeQuery();

			List<Object> list = new ArrayList<>();
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

	private Object executeSelectOneQuery(String sql, RowMapper rm, Object... parameters) throws SQLException {
		List<Object> list = executeSelectAllQuery(sql, rm, parameters);
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
