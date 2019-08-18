import org.junit.jupiter.api.Test;
import support.exception.FunctionWithException;


import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilTest {

    @Test
    void functionWithExceptionTest() {
        FunctionWithException<String, Integer, Exception> fwe = this::positiveLengthMapper;

        assertThrows(RuntimeException.class, () -> {
            try {
                fwe.apply("");
            } catch (Exception e) {
                throw new RuntimeException("converting runtime exception");
            }
        });

    }

    private Integer positiveLengthMapper(String s) throws Exception {

        if (s.length() > 0) {
            return s.length();
        }

        throw new Exception();
    }

}
