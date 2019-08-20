package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface JdbcTypeHandler<T> {

	public boolean supports(Class<?> clazz);
	
	public void setParameter(PreparedStatement ps, int index, Object value) throws SQLException;
	
	public T getParameter(ResultSet rs, int index) throws SQLException;

	public T getParameter(ResultSet rs, String columnLabel) throws SQLException;
	
}
