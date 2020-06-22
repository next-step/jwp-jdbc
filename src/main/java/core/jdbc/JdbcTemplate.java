package core.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public <T> List<T> queryForList(String query, Object[] arguments, RowMapper<T> rowMapper) {
        return this.query(query, arguments, new RowMapperResultSetExtractor(rowMapper));
    }

    public <T> T queryForObject(String query, Object[] arguments, RowMapper<T> rowMapper) {
        List<T> resultList = this.query(query, arguments, new RowMapperResultSetExtractor(rowMapper));

        if (resultList == null || resultList.isEmpty()) {
            return null;
        }

        if (resultList.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, resultList.size());
        }

        return resultList.iterator().next();
    }

    public <T> T query(String query, Object[] arguments, ResultSetExtractor rse) {
        return this.execute(query, preparedStatement -> {
            ResultSet rs = null;

            Object result;
            try {

                setValues(preparedStatement, arguments);

                rs = preparedStatement.executeQuery();
                result = rse.extractData(rs);
            } finally {
                if (rs != null) {
                    rs.close();
                }
            }
            return (T) result;
        });
    }

    public int update(String query, Object[] arguments) {
        return this.execute(query, preparedStatement -> {
            setValues(preparedStatement, arguments);
            int updatedRows = preparedStatement.executeUpdate();
            return updatedRows;
        });
    }

    private <T> T execute(String query, PreparedStatementCallback<T> callback) {
        Connection connection = ConnectionManager.getConnection(dataSource);
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(query);
            T result = callback.doInstatement(preparedStatement);

            connection.commit();

            return result;
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                logger.error("Rollback failed", ex);
                throw new JdbcTemplateException("Rollback failed", ex);
            }
            logger.error("Sql Exception", e);
            throw new JdbcTemplateException("Sql Exception", e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logger.error("Jdbc close failed", ex);
                throw new JdbcTemplateException("Jdbc close failed", ex);
            }
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
}
