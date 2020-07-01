package core.mvc.tobe.support;

import core.mvc.tobe.MethodParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class RequestResponseArgumentResolverTest {

    private List<ArgumentResolver> argumentResolvers;

    @BeforeEach
    void setUp() {
        argumentResolvers = asList(
                new HttpRequestArgumentResolver(),
                new HttpResponseArgumentResolver(),
                new RequestParamArgumentResolver(),
                new PathVariableArgumentResolver(),
                new ModelArgumentResolver()
        );
    }

    @DisplayName("Request Type Argument Resolver Test")
    @Test
    void requestResolveArguments() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        MethodParameter mp = new MethodParameter(null, HttpServletRequest.class, new Annotation[0], "");
        Object result = resolveArgument(mp, request, response);

        assertThat(result).isEqualTo(request);
    }

    @DisplayName("Response Type Argument Resolver Test")
    @Test
    void responseResolveArguments() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        MethodParameter mp = new MethodParameter(null, HttpServletResponse.class, new Annotation[0], "");
        Object result = resolveArgument(mp, request, response);

        assertThat(result).isEqualTo(response);
    }

    private Object resolveArgument(MethodParameter mp, HttpServletRequest request, HttpServletResponse response) {
        for (ArgumentResolver argumentResolver : argumentResolvers) {
            if (argumentResolver.supports(mp)) {
                return argumentResolver.resolveArgument(mp, request, response);
            }
        }

        throw new IllegalArgumentException("does not have argumentResolver");
    }

}
