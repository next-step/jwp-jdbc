package next.dao;

import next.jdbc.JdbcTemplate;
import next.jdbc.OrderIndexSupplier;
import next.jdbc.StatementSupplier;
import next.jdbc.TypeMapper;
import next.model.User;

import java.util.List;

public class UserDao {

    private static final TypeMapper<User> userTypeMapper = new TypeMapper<>(User.class);

    private final JdbcTemplate jdbcTemplate;

    public UserDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(final User user) {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        final StatementSupplier supplier = new OrderIndexSupplier(user.getUserId(), user.getPassword(), user.getName(),
                user.getEmail());

        jdbcTemplate.execute(sql, supplier);
    }

    public void update(final User user) {
        final String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        final StatementSupplier supplier = new OrderIndexSupplier(user.getPassword(), user.getName(), user.getEmail(),
                user.getUserId());

        jdbcTemplate.execute(sql, supplier);
    }

    public List<User> findAll() {
        final String sql = "SELECT userId, password, name, email FROM USERS";

        return jdbcTemplate.queryList(sql, userTypeMapper);
    }

    public User findByUserId(final String userId) {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        final StatementSupplier supplier = new OrderIndexSupplier(userId);

        return jdbcTemplate.querySingle(sql, supplier, userTypeMapper)
                .orElseThrow();
    }
}
