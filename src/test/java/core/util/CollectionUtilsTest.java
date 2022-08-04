package core.util;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CollectionUtilsTest {
    @Test
    void getFirstElement_Collection의_첫번째_element를_가져온다() {
        String result = CollectionUtils.getFirstElement(Set.of("value"));
        assertThat(result).isEqualTo("value");
    }

    @Test
    void getFirstElement_빈_Collection은_IllegalArgumentException을_throw_한다() {
        assertThatThrownBy(() -> CollectionUtils.getFirstElement(Set.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
