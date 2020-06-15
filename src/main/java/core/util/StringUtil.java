package core.util;

public class StringUtil {

    public static String upperFirstChar(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }


}
