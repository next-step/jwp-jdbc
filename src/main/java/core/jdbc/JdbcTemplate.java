package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import core.exception.JdbcException;

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
		});
	}

	public <T> Optional<T> select(String sql, RowMapper<T> rowMapper, Object... args) {

		List<T> values = selectList(sql, rowMapper, args);

		if(values.size() > 1) {
			throw new JdbcException("has multipleRows!!!");
		}

		return values.stream()
				.findFirst();

	}

	public int update(String sql, Object... args) {
		return execute(sql, new UpdateQueryAction(), new ParameterSetter(args), (count) -> count);
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
