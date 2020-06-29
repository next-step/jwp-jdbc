package core.util;

public class StringUtils {

    public static final String EMPTY = "";

    public static String upperFirstChar(String word) {
        if (word == null || word.isEmpty()) {
            return EMPTY;
        }

        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

}
