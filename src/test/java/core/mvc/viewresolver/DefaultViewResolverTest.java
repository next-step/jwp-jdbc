package core.mvc.viewresolver;

import core.mvc.JspView;
import core.mvc.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultViewResolverTest {

    private DefaultViewResolver defaultViewResolver;

    @BeforeEach
    void setup() {
        this.defaultViewResolver = new DefaultViewResolver();
    }

    @DisplayName("supports - 항상 true")
    @Test
    void supports() throws Exception {
        assertThat(defaultViewResolver.supports(null)).isTrue();
    }

    @DisplayName("resolve - JspView 가 반환된다.")
    @Test
    void resolve() throws Exception {
        View result = defaultViewResolver.resolve("");

        assertThat(result).isInstanceOf(JspView.class);
    }
}