package core.mvc.tobe;

import core.mvc.asis.RequestMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author KingCjy
 */
public class HandlerMappingCompositeTest {

    private HandlerMappingComposite handlerMappingComposite;

    @BeforeEach
    public void init() {
        RequestMapping requestMapping = new RequestMapping();
        requestMapping.initialize();

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        annotationHandlerMapping.initialize();

        handlerMappingComposite = new HandlerMappingComposite(annotationHandlerMapping, requestMapping);
    }

//    @Test
//    @DisplayName("기존 레거시 RequestMapping 사용")
//    public void getHandlerFromRequestMapping() {
//        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/");
//
//        HandlerExecution handler = handlerMappingComposite.getHandler(request);
//
//        assertThat(handler).isInstanceOf(LegacyHandlerExecution.class);
//    }

    @Test
    @DisplayName("신규 AnnotationHandlerMapping 사용")
    public void getHandlerFromAnnotationHandlerMapping() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");

        HandlerExecution handler = handlerMappingComposite.getHandler(request);

        assertThat(handler).isInstanceOf(HandlerExecutionImpl.class);
    }

    @Test
    @DisplayName("404 테스트")
    public void throwPageNotFoundException() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/test/hi/do");

        assertThatThrownBy(() -> {
            HandlerExecution handler = handlerMappingComposite.getHandler(request);
        }).isInstanceOf(PageNotFoundException.class);
    }
}
