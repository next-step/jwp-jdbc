package next.dao;

public class JdbcParameter {
    private final int index;

    private final Object value;

    public JdbcParameter(int index, Object value) {
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
