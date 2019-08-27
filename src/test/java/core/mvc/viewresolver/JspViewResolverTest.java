package core.mvc.viewresolver;

import core.mvc.JspView;
import core.mvc.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

class JspViewResolverTest {

    private JspViewResolver jspViewResolver;

    @BeforeEach
    void setup() {
        this.jspViewResolver = new JspViewResolver();
    }

    @DisplayName("supports - viewName 이 .jsp 로 끝나면 true")
    @Test
    void supports() throws Exception {
        String viewName = "template.jsp";

        boolean result = jspViewResolver.supports(viewName);

        assertThat(result).isTrue();
    }

    @DisplayName("supports - viewName 이 .jsp 로 끝나지 않으면 false")
    @Test
    void supports_not() throws Exception {
        String viewName = "template.html";

        boolean result = jspViewResolver.supports(viewName);

        assertThat(result).isFalse();
    }

    @DisplayName("resolve - JspView 가 반환된다.")
    @Test
    void resolve() throws Exception {
        View result = jspViewResolver.resolve("");

        assertThat(result).isInstanceOf(JspView.class);
    }
}