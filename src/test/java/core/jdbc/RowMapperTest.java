package core.jdbc;

import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RowMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(RowMapperTest.class);
    public static final String USERID = "catsbi";
    public static final String PASSWORD = "1q2w3e";
    public static final String NAME = "hansol";
    public static final String EMAIL = "catsbi@next.com";

    @Mock
    private ResultSet rs;

    @BeforeEach
    public void setup() throws SQLException {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        fixtureSetUp();
    }

    private void fixtureSetUp() throws SQLException {
        when(rs.next()).thenReturn(true);
        when(rs.getString("userId")).thenReturn(USERID);
        when(rs.getString("password")).thenReturn(PASSWORD);
        when(rs.getString("name")).thenReturn(NAME);
        when(rs.getString("email")).thenReturn(EMAIL);

    }

    @DisplayName("익명 클래스를 이용해 매핑 로직을 제공할 수 있다.")
    @Test
    void rowMappingWithAnonymous() throws Exception {
        RowMapper<User> rowMapper = new RowMapper<>() {
            @Override
            public User mapping(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    return new User(rs.getString("userId"), rs.getString("password"),
                            rs.getString("name"), rs.getString("email"));
                }
                return null;
            }
        };


        User user = rowMapper.mapping(rs);
        logger.info("user: {}", user);

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(USERID);
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
    }

    @DisplayName("람다식을 이용해 매핑 로직을 제공할 수 있다.")
    @Test
    void rowMappingWithLambda() throws Exception {
        RowMapper<User> rowMapper = rs->{
            if (rs.next()) {
                return new User(rs.getString("userId"), rs.getString("password"),
                        rs.getString("name"), rs.getString("email"));
            }
            return null;
        };

        User user = rowMapper.mapping(rs);

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(USERID);
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
    }
}