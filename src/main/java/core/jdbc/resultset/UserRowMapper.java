package core.jdbc.resultset;

import core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = User.builder()
            .userId(rs.getString(1))
            .password(rs.getString(2))
            .name(rs.getString(3))
            .email(rs.getString(4))
            .build();

        log.debug("user: {}", StringUtils.toPrettyJson(user));

        return user;
    }
}
