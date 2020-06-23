package core.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * @author KingCjy
 */
public class JdbcTemplate {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> List<T> queryForList(String query, RowMapper<T> rowMapper) {
        return this.queryForList(query, new Object[]{}, rowMapper);
    }

    public <T> List<T> queryForList(String query, Object[] arguments, RowMapper<T> rowMapper) {
        return this.query(query, arguments, new RowMapperResultSetExtractor(rowMapper));
    }

    public <T> T queryForObject(String query, RowMapper<T> rowMapper) {
        return this.queryForObject(query, rowMapper);
    }

    public <T> T queryForObject(String query, RowMapper<T> rowMapper, Object ...arguments) {
        return getSingleResult(this.query(query, arguments, new RowMapperResultSetExtractor(rowMapper)));
    }

    private <T> T query(String query, Object[] arguments, ResultSetExtractor rse) {
        return this.execute(query, arguments, preparedStatement -> {
            ResultSet rs = preparedStatement.executeQuery();
            return (T) rse.extractData(rs);
        });
    }

    public int update(String query, Object... arguments) {
        return update(query, null, arguments);
    }

    public int update(String query, KeyHolder keyHolder, Object... arguments) {
        return this.execute(query, arguments, preparedStatement -> {
            int updatedRows = preparedStatement.executeUpdate();

            if (keyHolder == null) {
                return updatedRows;
            }

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    long generatedKey = rs.getLong(1);
                    logger.debug("Generated Key: {}", generatedKey);
                    keyHolder.setId(generatedKey);
                }
            }

            return updatedRows;
        });
    }

    private <T> T execute(String query, Object[] arguments, PreparedStatementCallback<T> callback) {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setValues(preparedStatement, arguments);

            T result = callback.doInstatement(preparedStatement);
            connection.commit();

            return result;
        } catch (SQLException e) {
            logger.error("Sql Exception", e);
            throw new DataAccessException("Sql Exception", e);
        } finally {
            DataSourceUtils.releaseConnection(connection);
        }
    }

    private void setValues(PreparedStatement preparedStatement, Object[] arguments) throws SQLException {
        if (arguments == null || arguments.length == 0) {
            return;
        }

        for (int i = 0; i < arguments.length; i++) {
            preparedStatement.setObject(i + 1, arguments[i]);
        }
    }

    private <T> T getSingleResult(List<T> result) {
        if (result == null || result.isEmpty()) {
            return null;
        }

        if (result.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, result.size());
        }

        return result.iterator().next();
    }
}
