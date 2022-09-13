package core.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PathAnalyzerTest {

    @Test
    void isTargetPath() {
        String path = "/api/*";
        String requestedUri = "/api/latency";

        Assertions.assertThat(PathAnalyzer.getInstance().isTargetPath(path, requestedUri)).isTrue();
    }

}
