package core.util;

public class StringUtil {

    private StringUtil() {}

    public static String upperFirstChar(String word) {
        if (isEmpty(word)) {
            return "";
        }

        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public static boolean isEmpty(String origin) {
        return origin == null || origin.isEmpty();
    }
}
