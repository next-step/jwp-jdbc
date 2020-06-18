package core.mvc.tobe;

import next.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author KingCjy
 */
public class HandlerExecutionImplTest {

    @Test
    public void handleTest() throws Exception {
        User user = new User("pobi", "password", "포비", "pobi@nextstep.camp");

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        request.setParameter("name", user.getName());
        request.setParameter("email", user.getEmail());
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution handlerExecution = new HandlerExecutionImpl(new MyController(), MyController.class.getDeclaredMethod("save", HttpServletRequest.class, HttpServletResponse.class));
        handlerExecution.handle(request, response);
    }
}
