package core.jdbc;


import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate implements JdbcOperations {

    @Override
    public void execute(String sql, Object... parameters) {
        ExecuteTemplate executeTemplate = new ExecuteTemplate() {
            @Override
            void setValues(PreparedStatement ps) throws SQLException {
                int i = 1;
                for (Object obj : parameters) {
                    ps.setString(i++, obj.toString());
                }
            }
        };
        executeTemplate.execute(sql);
    }

    @Override
    public <T> List<T> queryForList(String sql, ResultSetExtractor<T> resultSetExtractor) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(resultSetExtractor.extractData(rs));
            }
            return results;
        } catch (SQLException e) {
            throw new JdbcExecuteException(e);
        }
    }

    public <T> List<T> queryForList2(String sql, ResultSetExtractor<T> resultSetExtractor) {
        SelectTemplate selectTemplate = new SelectTemplate() {
            @Override
            void setValues(PreparedStatement ps) { }

            @Override
            List<User> getResultMapper(ResultSetExtractor extractor, ResultSet rs) throws SQLException {
                List<User> results = new ArrayList<>();
                while (rs.next()) {
                    results.add((User) resultSetExtractor.extractData(rs));
                }
                return results;
            }
        };
        return selectTemplate.execute(sql, resultSetExtractor);
    }

    @Override
    public <R> Optional<R> queryForObject(String sql, ResultSetExtractor<R> resultSetExtractor, Object... parameters) {
        SelectTemplate template = new SelectTemplate() {
            @Override
            void setValues(PreparedStatement ps) throws SQLException {
                int i = 1;
                for (Object obj : parameters) {
                    ps.setString(i++, obj.toString());
                }
            }

            @Override
            Optional<User> getResultMapper(ResultSetExtractor extractor, ResultSet rs) throws SQLException {
                if (rs.next()) {
                    return (Optional<User>) Optional.ofNullable(resultSetExtractor.extractData(rs));
                }
                return Optional.empty();
            }
        };
        return template.execute(sql, resultSetExtractor);
    }
}