package core.jdbc;

import core.jdbc.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class JdbcTemplate implements Operations {

    Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    public <T> T execute(String query, RowMapper<T> rowMapper, Object... objects) {

        try (Connection con = ConnectionManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(query);

            IntStream.range(0, objects.length)
                    .forEach(index -> setObject(pstmt, index + 1, objects[index]));

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }

        } catch (SQLException e) {
            log.error("[query execute failed] query={}", query, e);
        }

        throw new DataAccessException();
    }

    @Override
    public <T> List<T> execute(String query, RowMapper<T> rowMapper) {
        try (Connection con = ConnectionManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            List<T> results = new ArrayList<>();
            if (rs.next()) {
                results.add(rowMapper.mapRow(rs));
                return results;
            }
        } catch (SQLException e) {
            log.error("[query execute failed] query={}", query, e);
        }

        throw new DataAccessException();
    }

    @Override
    public void update(String query, Object... objects) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            IntStream.range(0, objects.length)
                    .forEach(index -> setObject(pstmt, index + 1, objects[index]));

            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("[query update failed] query={}", query, e);
            throw new DataAccessException();
        }
    }

    @Override
    public void insert(String query, Object... objects) {
        try (Connection con = ConnectionManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(query);

            IntStream.range(0, objects.length)
                    .forEach(index -> setObject(pstmt, index + 1, objects[index]));

            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("[query insert failed] query={}", query, e);
            throw new DataAccessException();
        }
    }

    private void setObject(PreparedStatement pstmt, int index, Object object) {
        try {
            pstmt.setObject(index, object);
        } catch (SQLException e) {
            log.error("set parameter failed", e);
        }
    }

}
