package core.jdbc;

import core.jdbc.exception.DataAccessException;
import core.jdbc.exception.SetDataParameterException;
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

    @Override
    public <T> T execute(String query, RowMapper<T> rowMapper, Object... objects) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            setParameters(pstmt, objects);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }

            return null;

        } catch (SQLException e) {
            log.error("[query execute failed] query={}", query, e);
            throw new DataAccessException();
        }
    }

    @Override
    public <T> List<T> execute(String query, RowMapper<T> rowMapper) {
        try (Connection con = ConnectionManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            List<T> results = new ArrayList<>();

            while (rs.next()) {
                results.add(rowMapper.mapRow(rs));
            }

            return results;
        } catch (SQLException e) {
            log.error("[query execute failed] query={}", query, e);
            throw new DataAccessException();
        }
    }

    @Override
    public void execute(String query, Object... objects) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            setParameters(pstmt, objects);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("[query update failed] query={}", query, e);
            throw new DataAccessException();
        }
    }

    private void setParameters(PreparedStatement pstmt, Object[] objects) {
        IntStream.range(0, objects.length)
                .forEach(index -> setParameter(pstmt, index + 1, objects[index]));
    }

    private void setParameter(PreparedStatement pstmt, int index, Object object) {
        try {
            pstmt.setObject(index, object);
        } catch (SQLException e) {
            log.error("set parameter failed", e);
            throw new SetDataParameterException();
        }
    }

}
