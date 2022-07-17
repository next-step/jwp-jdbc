package core.util;

import java.util.Collection;

public class CollectionUtils {
    private CollectionUtils() {
    }

    public static <T> T getFirstElement(Collection<T> collection) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("Cannot get any value from empty collection");
        }

        return collection.iterator().next();
    }
}
