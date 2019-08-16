package next.controller;

import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    private static final Logger logger = LoggerFactory.getLogger(UserAcceptanceTest.class);

    @Override
    protected String getServerBaseUrl() {
        return "http://localhost:8080";
    }

    @Test
    @DisplayName("사용자 회원가입/조회/수정/삭제")
    void crud() {
        // 회원가입
        User insertUser = new User("jun", "password", "현준", "homelus@daum.net");
        URI location = post("/api/user", insertUser, User.class);
        logger.debug("location : {}", location); // /api/users?userId=pobi 와 같은 형태로 반환
        // 조회
        User actual = get(location.toString(), User.class);
        testUserEqual(actual, insertUser);
        // 수정
        User updateUser = new User("jun", "test", "콩콩", "homelus@daum.net");
        put(location.toString(), updateUser, User.class);
        actual = get(location.toString(), User.class);
        testUserEqual(actual, updateUser);
    }

    void testUserEqual(User actual, User expected) {
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }

}
