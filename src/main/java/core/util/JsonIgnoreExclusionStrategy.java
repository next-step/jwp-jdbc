package core.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class JsonIgnoreExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(JsonIgnore.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return clazz.isAnnotationPresent(JsonIgnore.class);
    }
}
