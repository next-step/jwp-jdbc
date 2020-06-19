package core.mvc.tobe;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author KingCjy
 */
public class ControllerScannerTest {

    @Test
    public void scanControllersTest() {
        ControllerScanner controllerScanner = new ControllerScanner("core.mvc.tobe");

        Map<Class<?>, Object> instantiateControllers = controllerScanner.getInstantiateControllers();

        assertThat(instantiateControllers).containsKeys(MyController.class);
    }
}
