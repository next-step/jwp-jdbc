package core.mvc.viewresolver;

import core.mvc.HandlebarsView;
import core.mvc.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HandlebarsViewResolverTest {

    private HandlebarsViewResolver handlebarsViewResolver;

    @BeforeEach
    void setup() {
        this.handlebarsViewResolver = new HandlebarsViewResolver();
    }

    @DisplayName("supports - viewName 이 .html 로 끝나면 true")
    @Test
    void supports() throws Exception {
        String viewName = "handlebars.html";

        boolean result = handlebarsViewResolver.supports(viewName);

        assertThat(result).isTrue();
    }

    @DisplayName("supports - viewName 이 .html 로 끝나지 않으면 false")
    @Test
    void supports_not() throws Exception {
        String viewName = "handlebars.jsp";

        boolean result = handlebarsViewResolver.supports(viewName);

        assertThat(result).isFalse();
    }

    @DisplayName("resolve - 반환 되는 view 는 HandlebarsView")
    @Test
    void resolve() throws Exception {
        View result = handlebarsViewResolver.resolve("");

        assertThat(result).isInstanceOf(HandlebarsView.class);
    }

}