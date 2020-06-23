package core.jdbc;

import core.jdbc.exceptions.UnableToAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            setArguments(pstmt, args);
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
        try (
                final Connection connection = dataSource.getConnection();
                final PreparedStatement pstmt = connection.prepareStatement(sql);
        ) {
            ResultSet rs;
            setArguments(pstmt, args);
            rs = pstmt.executeQuery();

            final List<T> results = new ArrayList<>();
            int rowNum = 0;
            while (rs.next()) {
                results.add(rowMapper.mapRow(rs, rowNum++));
            }
            return results;
        } catch (SQLException throwables) {
            throw new UnableToAccessException(":'(");
        }
    }

    @Override
    public int update(String sql, Object... args) throws UnableToAccessException {
        try (
                final Connection connection = dataSource.getConnection();
                final PreparedStatement pstmt = connection.prepareStatement(sql);
        ) {
            setArguments(pstmt, args);
            final int affectedRows = pstmt.executeUpdate();
            log.debug("affected rows: {}", affectedRows);
            return affectedRows;
        } catch (SQLException throwables) {
            throw new UnableToAccessException("메세지는 나중에 적자..");
        }
    }

    private void setArguments(PreparedStatement pstmt, Object[] values) throws SQLException {
        final int length = values != null ? values.length : 0;
        for (int i = 1; i <= length; i++) {
            pstmt.setObject(i, values[i - 1]);
        }
    }

}
