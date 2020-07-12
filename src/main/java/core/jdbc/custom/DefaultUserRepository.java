package core.jdbc.custom;

import core.jdbc.custom.query.FindAllQueryGenerator;
import core.jdbc.custom.query.SaveQueryGenerator;
import next.model.User;

import java.sql.SQLException;
import java.util.List;

public class DefaultUserRepository {

    private JdbcTemplate<User> jdbcTemplate;
    private RowMapper<User> defaultRowMapper;

    public DefaultUserRepository() {
        this.jdbcTemplate = new JdbcTemplate<>();
        this.defaultRowMapper = new DefaultRowMapper<>(User.class);
    }

    public void save(final User user) {
        jdbcTemplate.save(new SaveQueryGenerator<>(user), new CreatePreparedStatementSetter<>(user));
    }


    public User findById(final String id) {
        return this.jdbcTemplate.queryForObject(() -> String.format("SELECT * FROM %s WHERE %s = ?", "USERS", id), preparedStatement -> {
            try {
                preparedStatement.setString(1, id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, defaultRowMapper);
    }

    public List<User> findAll() {
        return this.jdbcTemplate.query(new FindAllQueryGenerator(User.class), defaultRowMapper);
    }
}
