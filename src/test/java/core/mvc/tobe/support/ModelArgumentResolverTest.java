package core.mvc.tobe.support;

import core.mvc.tobe.MethodParameter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ModelArgumentResolverTest {

    ModelArgumentResolver argumentResolver = new ModelArgumentResolver();

    @Test
    void modelArgumentResolver() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String expectedId = "jun";
        String expectedName = "hyunjun";
        String expectedAge = "12";
        String expectedMoney = "10000000000";

        request.setParameter("id", expectedId);
        request.setParameter("name", expectedName);
        request.setParameter("age", expectedAge);
        request.setParameter("money", expectedMoney);
        request.setParameter("notSetterPhoneNumber", "00012345678");

        final Method modelMethod = MockArgumentResolverController.class.getDeclaredMethod("mockUser", MockUser.class);

        MethodParameter mp = new MethodParameter(modelMethod, MockUser.class, new Annotation[]{}, "user");

        MockUser user = (MockUser) argumentResolver.resolveArgument(mp, request, response);

        assertThat(user.getId()).isEqualTo(expectedId);
        assertThat(user.getName()).isEqualTo(expectedName);
        assertNull(user.getAddr());
        assertNull(user.getNotSetterPhoneNumber());
        assertThat(user.getAge()).isEqualTo(Integer.valueOf(expectedAge));
        assertThat(user.getMoney()).isEqualTo(Long.valueOf(expectedMoney));

    }


}
