package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class StringJdbcTypeHandler extends AbstractJdbcTypeHandler<String>{

	public StringJdbcTypeHandler() {
		super(String.class, Types.VARCHAR);
	}
	
	@Override
	public void setParameterType(PreparedStatement ps, int index, String value) throws SQLException {
		ps.setString(index, value);
	}
	
	@Override
	public String getParameter(ResultSet rs, int index) throws SQLException {
		return rs.getString(index);
	}

	@Override
	public String getParameter(ResultSet rs, String columnLabel) throws SQLException {
		return rs.getString(columnLabel);
	}
}
