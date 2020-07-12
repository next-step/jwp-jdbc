package core.jdbc.custom.query;

public class FindAllQueryGenerator implements QueryGenerator {

    Class clazz;

    public FindAllQueryGenerator(final Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public String make() {
        return String.format("SELECT * FROM %sS", clazz.getSimpleName());
    }
}
