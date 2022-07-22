package next.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreparedStatementValues {
    private final List<PreparedStatementValue> preparedStatementValues;

    public PreparedStatementValues(Object value) {
        this.preparedStatementValues = List.of(new PreparedStatementValue(1, value));
    }

    public PreparedStatementValues(List<Object> values) {
        List<PreparedStatementValue> preparedStatementValues = new ArrayList<>();
        for (int index = 1; index <= values.size(); index++) {
            preparedStatementValues.add(new PreparedStatementValue(index, values.get(index - 1)));
        }
        this.preparedStatementValues = preparedStatementValues;
    }

    public List<PreparedStatementValue> getValues() {
        return Collections.unmodifiableList(preparedStatementValues);
    }
}
