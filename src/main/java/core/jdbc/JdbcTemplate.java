package core.jdbc;

import core.exception.JdbcException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
	private final DataSource dataSource;

	public JdbcTemplate(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public <T> List<T> selectList(String sql, RowMapper<T> rowMapper, Object... args) {
		return execute(sql, new SelectQueryAction(), new ParameterSetter(args), (rs) -> {
				List<T> tempList = new ArrayList<>();
				while(rs.next()) {
					tempList.add(rowMapper.resultMapping(rs));
				}
				return tempList;
			}
		);
	}

	public <T> T select(String sql, RowMapper<T> rowMapper, Object... args) {
		return execute(sql, new SelectQueryAction(), new ParameterSetter(args), (rs) -> {
				T value = null;

		        while(rs.next()) {

		            if(value != null) {
		                throw new SQLException("has multipleRows!!!");
		            }

		            value = rowMapper.resultMapping(rs);
		        }

		        return value;
			}
		);
	}

	public int update(String sql, Object... args) {
		return execute(sql, new UpdateQueryAction(), new ParameterSetter(args), new ResultMapper<Integer, Integer>() {

			@Override
			public Integer resultMapping(Integer result) throws SQLException {
				return result;
			}
		});
	}

	private<T, R> R execute(String sql, QueryAction<T> queryAction, ParameterSetter parameterSetter, ResultMapper<T, R> resultMapper) {
		try(Connection connection = this.dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)
				) {

			setArgsIfSupport(preparedStatement, parameterSetter);
			T result = queryAction.action(preparedStatement);

			return resultMapper.resultMapping(result);
		} catch (SQLException e) {
			throw new JdbcException(e);
		}
	}

	private void setArgsIfSupport(PreparedStatement preparedStatement, ParameterSetter parameterSetter) throws SQLException {
		if(parameterSetter != null) {
			parameterSetter.setArgs(preparedStatement);
		}
	}
}
