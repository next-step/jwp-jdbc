package core.jdbc;

import core.jdbc.exception.JdbcTemplateException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
public class JdbcTemplate {

    private DataSource dataSource;
    private Connection connection;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initConnection() {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new JdbcTemplateException("Data source connection initialization failed.", e);
        }
    }

    public void setConnectionAutoCommit(boolean autoCommit) {
        try {
            if (this.connection == null) {
                initConnection();
            }

            this.connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            throw new JdbcTemplateException("connection setting auto commit failed.", e);
        }
    }

    public void update(String sql) {
        update(sql, new Object[]{});
    }

    public void update(String sql, Object[] parameters) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            log.info("execute update query = {}", sql);

            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    public void bulkUpdate(List<String> sqls) {
        try {
            for (String sql : sqls) {
                log.info("execute update query = {}", sql);

                PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {
        return queryForObject(sql, new Object[]{}, rowMapper);
    }

    public <T> T queryForObject(String sql, Object[] parameters, RowMapper<T> rowMapper) {
        List<T> objects = executeSelect(sql, parameters, rowMapper);
        if (objects.isEmpty()) {
            return null;
        }

        return objects.get(0);
    }

    public <T> List<T> query(String sql, Object[] parameters, RowMapper<T> rowMapper) {
        return executeSelect(sql, parameters, rowMapper);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return executeSelect(sql, new Object[]{}, rowMapper);
    }

    public void closeConnection() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException e) {
            throw new JdbcTemplateException("connection close failed.", e);
        }
    }

    private <T> List<T> executeSelect(String sql, Object[] parameters, RowMapper<T> rowMapper) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }

            log.info("execute select query = {}", sql);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<T> objects = new ArrayList<>();

                while (resultSet.next()) {
                    T object = rowMapper.mapRow(resultSet);
                    objects.add(object);
                }

                return objects;
            }
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

}
