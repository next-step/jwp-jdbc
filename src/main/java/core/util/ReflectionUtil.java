package core.util;

import java.lang.reflect.Method;

public class ReflectionUtil {

    public static String setMethodNameByField(String fieldName) {
        return "set" + upperFirstChar(fieldName);
    }

    private static String upperFirstChar(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    private static String lowerFirstChar(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        return word.substring(0, 1).toLowerCase() + word.substring(1);
    }

    public static String getFieldBySetMethodName(String methodName) {
        if (!methodName.startsWith("set")) {
            throw new IllegalArgumentException("[" + methodName + "] does not start with 'set'");
        }

        String fieldName = methodName.substring(3);
        return lowerFirstChar(fieldName);
    }

}
