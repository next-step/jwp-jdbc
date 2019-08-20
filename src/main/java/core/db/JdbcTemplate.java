package core.db;

import com.google.common.collect.Lists;
import core.jdbc.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    public void insert(String sql, Object... params) {
        try(Connection connection = ConnectionManager.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            int result = pstmt.executeUpdate();
            logger.debug("result : {}", result);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void update(String sql, Object... params) {
        try(Connection connection = ConnectionManager.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            int result = pstmt.executeUpdate();
            logger.debug("result : {}", result);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, @Nullable Object... params) {
        List<T> result = Lists.newArrayList();

        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            try(ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    result.add(rowMapper.mapRow(resultSet, resultSet.getRow()));
                }
            }

        } catch (Exception e) {

        }

        return result;
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, @Nullable Object... params) {
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            try(ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return rowMapper.mapRow(resultSet, resultSet.getRow());
                }
            }

        } catch (Exception e) {

        }

        return null;
    }
}
