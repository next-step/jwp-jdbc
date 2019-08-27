package core.mvc.viewresolver;

import core.mvc.JspView;
import core.mvc.View;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ViewResolverRegistryTest {

    private ViewResolverRegistry viewResolverRegistry;

    @DisplayName("resolveView - 지원하는 viewResolver 가 있으면 지원되는 viewResolver 의 반환값이 반환된다.")
    @Test
    void resolveView() throws Exception {
        boolean supports = true;

        viewResolverRegistry = new ViewResolverRegistry(new MockViewResolver(supports));

        View result = viewResolverRegistry.resolveView("");

        assertThat(result).isInstanceOf(TestView.class);
    }

    @DisplayName("resolveView - 지원하는 viewResolver 가 없으면 DefaultViewResolver 의 반환값인 JspView 가 반환된다.")
    @Test
    void resolveView_not_support() throws Exception {
        boolean supports = false;

        viewResolverRegistry = new ViewResolverRegistry(new MockViewResolver(supports));

        View result = viewResolverRegistry.resolveView("");

        assertThat(result).isInstanceOf(JspView.class);
    }

    private static class MockViewResolver implements ViewResolver {

        private final boolean supports;

        private MockViewResolver(boolean supports) {
            this.supports = supports;
        }

        @Override
        public boolean supports(String viewName) {
            return this.supports;
        }

        @Override
        public View resolve(String viewName) {
            return new TestView();
        }
    }

    private static class TestView implements View {

        @Override
        public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        }
    }
}