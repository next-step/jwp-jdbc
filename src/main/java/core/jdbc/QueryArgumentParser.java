package core.jdbc;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

final class QueryArgumentParser {

    private static final String ARGUMENT_NAME = "argument";
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile(String.format("\\#\\{(?<%s>\\w+)}", ARGUMENT_NAME));

    private final String sql;

    private String questionSymbolArgumentsSql;
    private Map<Integer, String> argumentKeys;

    private QueryArgumentParser(String sql) {
        Assert.hasText(sql, "'sql' must not be blank");
        this.sql = sql;
    }

    static QueryArgumentParser from(String sql) {
        return new QueryArgumentParser(sql);
    }

    String questionSymbolArgumentsSql() {
        if (questionSymbolArgumentsSql == null) {
            compile();
        }
        return questionSymbolArgumentsSql;
    }

    Map<Integer, Object> arguments(Map<String, Object> data) {
        Map<Integer, String> keys = argumentKeys();
        if (!data.keySet().containsAll(keys.values())) {
            throw new IllegalArgumentException(String.format("data(%s) must be contained all arguments(%s)", data, keys));
        }
        return keys.entrySet()
                .stream()
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, keyEntry -> data.get(keyEntry.getValue())));
    }

    private Map<Integer, String> argumentKeys() {
        if (argumentKeys == null) {
            compile();
        }
        return argumentKeys;
    }

    private void compile() {
        Map<Integer, String> keys = new HashMap<>();
        Matcher matcher = ARGUMENT_PATTERN.matcher(sql);
        int index = 1;
        while (matcher.find()) {
            keys.put(index++, matcher.group(ARGUMENT_NAME));
        }
        this.argumentKeys = keys;
        this.questionSymbolArgumentsSql = matcher.replaceAll("?");
    }
}
