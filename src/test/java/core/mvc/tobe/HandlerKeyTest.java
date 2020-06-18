package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlerKeyTest {

    @Test
    public void matchTest() {
        HandlerKey handlerKey = new HandlerKey("/users/{id}", RequestMethod.GET);

        assertThat(handlerKey.matches("/users/1", RequestMethod.GET)).isTrue();
        assertThat(handlerKey.matches("/users/1/hi", RequestMethod.GET)).isFalse();
    }
}
