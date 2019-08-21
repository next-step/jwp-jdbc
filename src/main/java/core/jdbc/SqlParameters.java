package core.jdbc;

import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SqlParameters {

    private static final int START_INCLUSIVE = 1;
    private List<SqlParameter> parameters;

    private SqlParameters(List<SqlParameter> parameters) {
        this.parameters = parameters;
    }

    static SqlParameters of(Object... parameters) {
        List<Object> objects = Arrays.asList(parameters);
        return IntStream.rangeClosed(START_INCLUSIVE, parameters.length)
                .mapToObj((num) -> SqlParameter.of(num, objects.get(num - START_INCLUSIVE)))
                .collect(Collectors.collectingAndThen(Collectors.toList(), SqlParameters::new));
    }

    List<SqlParameter> toList() {
        return Collections.unmodifiableList(parameters);
    }

    @Override
    public String toString() {
        return "SqlParameters{" +
                "parameters=" + parameters +
                '}';
    }

    static class SqlParameter {
        private int index;
        private String value;

        private SqlParameter(int index, String value) {
            this.index = index;
            this.value = value;
        }

        static SqlParameter of(int index, Object value) {
            return new SqlParameter(index, value.toString());
        }

        int getIndex() {
            return index;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "SqlParameter{" +
                    "index=" + index +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
