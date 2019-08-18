package core.util;

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

}
