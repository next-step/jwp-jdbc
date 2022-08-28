package core.mvc.tobe.interceptor;

import core.annotation.web.RequestMethod;
import core.mvc.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import next.controller.UserSessionUtils;
import next.model.User;
import next.model.exception.UpdateAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateUserAuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UpdateUserAuthenticationInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if (!request.getMethod().equals(RequestMethod.PUT.name())) {
            return true;
        }

        return isSameUser(request.getParameter("userId"), loggedInUser(request));
    }

    private static User loggedInUser(final HttpServletRequest request) {
        return UserSessionUtils.getUserFromSession(request.getSession());
    }

    private static boolean isSameUser(final String updateTargetUserId, final User userFromSession) {
        validateLoggedIn(userFromSession);

        final User updateTargetUser = new User(updateTargetUserId, null, null, null);

        if (!userFromSession.isSameUser(updateTargetUser)) {
            throw new UpdateAuthenticationException();
        }

        return true;
    }

    private static void validateLoggedIn(final User userFromSession) {
        if (userFromSession == null) {
            throw new UpdateAuthenticationException();
        }
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception exception)
        throws Exception {
        if (exception instanceof UpdateAuthenticationException) {
            final String userId = getLoggedInUserId(request);
            final String updateTargetUserId = request.getParameter("userId");
            logger.info("다른 회원의 정보를 수정하려는 시도가 있습니다. userId: {}, targetUserId: {}", userId, updateTargetUserId);
        }
    }

    private static String getLoggedInUserId(final HttpServletRequest request) {
        final User user = loggedInUser(request);
        if (user == null) {
            return "not logged-in anonymous user";
        }
        return user.getUserId();
    }
}
