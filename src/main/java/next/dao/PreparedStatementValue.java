package next.dao;

public class PreparedStatementValue {
    private final int index;

    private final Object value;

    public PreparedStatementValue(int index, Object value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public Object getValue() {
        return value;
    }
}
