package core.jdbc;

import core.jdbc.exceptions.UnableToAccessException;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * JDBC operations
 *
 * @author hyeyoom
 */
public interface JdbcOperation {

    /**
     * Query for single object
     *
     * @param sql       SQL query to execute
     * @param rowMapper Callback function for mapping. rowMapper that will map the object per row.
     * @param pss       Mapping function that maps values to PreparedStatement
     * @param <T>       Type of object
     * @return An Object that is type T
     * @throws UnableToAccessException ,_,
     */
    <T> T queryForSingleObject(String sql, RowMapper<T> rowMapper, @Nullable PreparedStatementSetter pss) throws UnableToAccessException;

    /**
     * Query for single object
     *
     * @param sql       SQL query to execute
     * @param rowMapper Callback function for mapping. rowMapper that will map the object per row.
     * @param args      Arguments to bind to the sql query. See the signature.
     * @param <T>       Type of object
     * @return An Object that is type T
     * @throws UnableToAccessException Well.. Some days are like that ,_,
     */
    <T> T queryForSingleObject(String sql, RowMapper<T> rowMapper, @Nullable Object... args) throws UnableToAccessException;

    /**
     * @param sql       SQL query to execute
     * @param rowMapper Callback function for mapping
     * @param pss       Mapping function that maps values to PreparedStatement
     * @param <T>       Type of object that will be mapped per row.
     * @return List of object
     * @throws UnableToAccessException ,_,
     */
    <T> List<T> query(String sql, RowMapper<T> rowMapper, @Nullable PreparedStatementSetter pss) throws UnableToAccessException;

    /**
     * Query for list of object
     *
     * @param sql       SQL query to execute
     * @param rowMapper Callback function for mapping
     * @param args      Arguments to bind to the sql query
     * @param <T>       Type of object that will be mapped per row.
     * @return List of object
     * @throws UnableToAccessException You are screwed._.
     */
    <T> List<T> query(String sql, RowMapper<T> rowMapper, @Nullable Object... args) throws UnableToAccessException;

    /**
     * @param sql SQL query to execute
     * @param pss Mapping function that maps values to PreparedStatement
     * @return Affected rows
     */
    int update(String sql, @Nullable PreparedStatementSetter pss);

    /**
     * Update operation
     *
     * @param sql  SQL query to execute
     * @param args Arguments to bind to the sql query above.
     * @return Affected rows
     * @throws UnableToAccessException You have a bad day. But It will be fine for sure.
     */
    int update(String sql, @Nullable Object... args) throws UnableToAccessException;
}
