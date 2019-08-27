package core.mvc.viewresolver;

import core.mvc.JsonView;
import core.mvc.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Java6Assertions.assertThat;

class ViewNameViewResolverTest {

    private ViewNameViewResolver viewNameViewResolver;

    @BeforeEach
    void setup() {
        this.viewNameViewResolver = new ViewNameViewResolver();
    }

    @DisplayName("supports - 등록된 View 이름이면 true")
    @ParameterizedTest
    @ValueSource(strings = {"jsonView"})
    void supports(String viewName) throws Exception {
        boolean result = viewNameViewResolver.supports(viewName);

        assertThat(result).isTrue();
    }

    @DisplayName("resolve - viewName 에 해당하는 View 가 반환된다.")
    @ParameterizedTest
    @MethodSource("getResolveTestArguments")
    void resolve(String viewName, Class<View> expectedView) throws Exception {
        View result = viewNameViewResolver.resolve(viewName);

        assertThat(result).isInstanceOf(expectedView);
    }

    private static Stream<Arguments> getResolveTestArguments() {
        return Stream.of(
                Arguments.of("jsonView", JsonView.class)
        );
    }
}