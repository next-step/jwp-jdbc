package core.db;

import com.google.common.collect.Lists;
import core.jdbc.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import support.exception.ExceptionConsumer;
import support.exception.ExceptionFunction;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);
    private static JdbcTemplate INSTANCE = new JdbcTemplate();

    private JdbcTemplate() {
    }

    public static JdbcTemplate getInstance() {
        return JdbcTemplate.INSTANCE;
    }

    public void update(String sql, @Nullable Object... params) {
        Consumer<PreparedStatement> consumer = ExceptionConsumer.wrap(
                preparedStatement -> executeQuery(preparedStatement, params)
        );

        acceptConsumer(consumer, sql);
    }

    public <T> List<T> selectMany(String sql, RowMapper<T> rowMapper, @Nullable Object... params) {
        Function<PreparedStatement, List<T>> function = ExceptionFunction.wrap(
                preparedStatement -> queryForList(preparedStatement, rowMapper, params)
        );

        return applyFunction(function, sql);
    }

    public <T> Optional<T> selectOne(String sql, RowMapper<T> rowMapper, @Nullable Object... params) {
        Function<PreparedStatement, Optional<T>> function = ExceptionFunction.wrap(
                preparedStatement ->
                        queryForList(preparedStatement, rowMapper, params).stream()
                                .findFirst()
        );

        return applyFunction(function, sql);
    }

    private <T> List<T> queryForList(PreparedStatement preparedStatement, RowMapper<T> rowMapper, Object[] params) throws SQLException {
        List<T> result = Lists.newArrayList();
        setParams(preparedStatement, params);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                result.add(rowMapper.mapRow(resultSet, resultSet.getRow()));
            }
        }

        return result;
    }

    private void setParams(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        if (Objects.isNull(params)) {
            return;
        }

        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    private void executeQuery(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        setParams(preparedStatement, params);
        int result = preparedStatement.executeUpdate();
        logger.debug("result count : {}", result);
    }

    private <T> T applyFunction(Function<PreparedStatement, T> function, String sql) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            return function.apply(preparedStatement);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void acceptConsumer(Consumer<PreparedStatement> consumer, String sql) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            consumer.accept(preparedStatement);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
