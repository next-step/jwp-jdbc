package core.mvc.tobe;

import core.mvc.JspViewResolver;
import core.mvc.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JspViewResolverTest {

    private JspViewResolver jspViewResolver;

    @BeforeEach
    public void initJspViewResolver() {
        jspViewResolver = new JspViewResolver();
    }

    @Test
    public void resolveTest() {
        View view = jspViewResolver.resolveViewName("test.jsp");

        assertThat(view).isNotNull();
    }

    @Test
    @DisplayName("ViewName이 .jsp로 끝나지 않을 시 null 반환")
    public void resolverNullTest() {
        View view = jspViewResolver.resolveViewName("redirect:/");

        assertThat(view).isNull();
    }
}
