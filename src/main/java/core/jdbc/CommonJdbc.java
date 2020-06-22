package core.jdbc;

import core.jdbc.exceptions.UnableToAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CommonJdbc implements JdbcOperation {

    private static final Logger log = LoggerFactory.getLogger(CommonJdbc.class);

    private DataSource dataSource;

    public CommonJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> T queryForSingleObject(String sql, RowMapper<T> rowMapper, Object... args) throws UnableToAccessException {
        ResultSet rs;
        try (
                final Connection connection = dataSource.getConnection();
                final PreparedStatement pstmt = connection.prepareStatement(sql);
        ) {
            final int argsLength = args != null ? args.length : 0;
            for (int i = 0; i < argsLength; i++) {
                // idx, value, type
                setArguments(pstmt, i + 1, args[i]);
            }
            rs = pstmt.executeQuery();

            T ret = null;
            if (rs.next()) {
                ret = rowMapper.mapRow(rs, 1);
            }
            rs.close();
            return ret;
        } catch (SQLException throwables) {
            throw new UnableToAccessException("Unable to access to datasource.", throwables);
        }
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws UnableToAccessException {
        return null;
    }

    @Override
    public int update(String sql, Object... args) throws UnableToAccessException {
        return 0;
    }

    private void setArguments(PreparedStatement pstmt, int idx, Object value) throws SQLException {
        // 응 bad smell 나도 알아 ㅇㅅㅇ
        final Class<?> clazz = value.getClass();
        if (clazz.isAssignableFrom(int.class) || clazz.isAssignableFrom(Integer.class)) {
            pstmt.setInt(idx, (Integer) value);
        }

        if (clazz.isAssignableFrom(long.class) || clazz.isAssignableFrom(Long.class)) {
            pstmt.setLong(idx, (Long) value);
        }

        if (clazz.isAssignableFrom(String.class)) {
            pstmt.setString(idx, (String) value);
        }
        pstmt.setObject(idx, value);
    }

}
