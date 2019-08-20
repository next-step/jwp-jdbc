package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractJdbcTypeHandler<T> implements JdbcTypeHandler<T>{

	private final Class<T> paramterizedType;
	private final int sqlType;
	
	public AbstractJdbcTypeHandler(Class<T> paramterizedType, int sqlType) {
		this.paramterizedType = paramterizedType;
		this.sqlType = sqlType;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return this.paramterizedType.isAssignableFrom(clazz);
	}
	
	@Override
	public void setParameter(PreparedStatement ps, int index, Object value) throws SQLException {
		
		if(value == null) {
			ps.setNull(index, this.sqlType);
		}
		
		setParameterType(ps, index, this.paramterizedType.cast(value));
	}
	
	public abstract void setParameterType(PreparedStatement ps, int index, T value) throws SQLException;
}
