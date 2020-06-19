package core.mvc.tobe;

import core.mvc.RedirectViewResolver;
import core.mvc.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class RedirectViewResolverTest {

    private RedirectViewResolver redirectViewResolver;

    @BeforeEach
    public void initRedirectViewResolver() {
        redirectViewResolver = new RedirectViewResolver();
    }

    @Test
    public void resolveTest() {
        View view = redirectViewResolver.resolveViewName("redirect:/");

        assertThat(view).isNotNull();
    }

    @Test
    @DisplayName("ViewName이 redirect: 로 시작하지 않을 시 null 반환")
    public void resolveNullTest() {
        View view = redirectViewResolver.resolveViewName("index.jsp");

        assertThat(view).isNull();
    }
}
