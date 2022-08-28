package core.mvc.tobe.interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import core.annotation.web.RequestMethod;
import javax.servlet.http.HttpSession;
import next.controller.UserSessionUtils;
import next.model.User;
import next.model.exception.UpdateAuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class UpdateUserAuthenticationInterceptorTest {

    private final UpdateUserAuthenticationInterceptor interceptor = new UpdateUserAuthenticationInterceptor();

    @DisplayName("RequestMethod가 PUT이 아니면 무조건 true를 리턴한다")
    @ParameterizedTest
    @EnumSource(value = RequestMethod.class, names = "PUT", mode = Mode.EXCLUDE)
    void request_method_is_not_put_then_returns_true(RequestMethod requestMethod) throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest(requestMethod.name(), "/api/users");
        final MockHttpServletResponse response = new MockHttpServletResponse();

        final boolean actual = interceptor.preHandle(request, response, null);

        assertThat(actual).isTrue();
    }

    @DisplayName("로그인한 유저와 회원정보 수정 대상 유저가 다른 경우 예외가 발생한다")
    @Test
    void exception_not_same_user() {
        final User loggedInUser = new User("admin", null, null, null);

        final MockHttpServletRequest request = new MockHttpServletRequest(RequestMethod.PUT.name(), "/api/users");
        request.setParameter("userId", "yongju");

        final HttpSession session = request.getSession(true);
        assert session != null;
        session.setAttribute(UserSessionUtils.USER_SESSION_KEY, loggedInUser);

        final MockHttpServletResponse response = new MockHttpServletResponse();

        assertThatThrownBy(() -> interceptor.preHandle(request, response, null))
            .isInstanceOf(UpdateAuthenticationException.class)
            .hasMessage("다른 사용자의 정보를 수정할 수 없습니다.");
    }

    @DisplayName("로그인하지 않은 상태에서 회원정보를 수정하면 예외가 발생한다")
    @Test
    void exception_not_logged_in_user() {
        final MockHttpServletRequest request = new MockHttpServletRequest(RequestMethod.PUT.name(), "/api/users");
        request.setParameter("userId", "yongju");

        final MockHttpServletResponse response = new MockHttpServletResponse();

        assertThatThrownBy(() -> interceptor.preHandle(request, response, null))
            .isInstanceOf(UpdateAuthenticationException.class)
            .hasMessage("다른 사용자의 정보를 수정할 수 없습니다.");
    }

    @DisplayName("인터셉터 에서 예외가 발생한 후 afterCompletion 메서드에서 로그를 남긴다")
    @Test
    void exception_log() throws Exception {
        // given
        final Logger logger = (Logger) LoggerFactory.getLogger(UpdateUserAuthenticationInterceptor.class);

        final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        final MockHttpServletRequest request = new MockHttpServletRequest(RequestMethod.PUT.name(), "/api/users");
        request.setParameter("userId", "yongju");

        final MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        interceptor.afterCompletion(request, response, null, new UpdateAuthenticationException());

        // then
        final ILoggingEvent actual = listAppender.list.get(0);

        assertThat(actual.getLevel()).isEqualTo(Level.INFO);
        assertThat(actual.getFormattedMessage()).isEqualTo(
            "다른 회원의 정보를 수정하려는 시도가 있습니다. userId: not logged-in anonymous user, targetUserId: yongju"
        );
    }

}
