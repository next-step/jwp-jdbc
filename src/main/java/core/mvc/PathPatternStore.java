package core.mvc;

import core.util.PathPatternUtil;

import java.util.HashSet;
import java.util.Set;

public class PathPatternStore {
    private final Set<String> store = new HashSet<>();

    public PathPatternStore() {}
    public PathPatternStore(Set<String> path) {
        store.addAll(path);
    }

    public void add(String path) {
        store.add(path);
    }

    public boolean match(String targetPath) {
        return store.stream()
                .anyMatch(path -> PathPatternUtil.isUrlMatch(path, targetPath));
    }
}
