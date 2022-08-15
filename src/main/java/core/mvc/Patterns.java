package core.mvc;

import core.util.PathPatternUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Patterns {
    private final Set<String> store = new HashSet<>();

    public Patterns() {}

    public Patterns(String... path) {
        Collections.addAll(store, path);
    }

    public void add(String path) {
        store.add(path);
    }

    public boolean matches(String targetPath) {
        return store.stream()
                .anyMatch(pattern-> PathPatternUtil.isUrlMatch(pattern, targetPath));
    }
}
