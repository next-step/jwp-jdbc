package core.jdbc;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UnderscoreConverter {

    public static String convertToUnderscore(String camelCase) {
        Preconditions.checkArgument(StringUtils.isNotBlank(camelCase), "빈 문자열 입니다");

        List<Integer> camelCaseIndexes = getCamelCaseIndexes(camelCase);
        return String.join("_", convert(camelCase, camelCaseIndexes));
    }

    private static List<Integer> getCamelCaseIndexes(String camelCase) {
        List<Integer> camelCaseIndexes = new ArrayList<>();
        if (camelCase.length() == 1) {
            return camelCaseIndexes;
        }

        char[] chars = camelCase.toCharArray();
        for (int i = 1; i < chars.length; i++) {
            if (Character.isUpperCase(chars[i])) {
                camelCaseIndexes.add(i);
            }
        }
        return camelCaseIndexes;
    }

    private static List<String> convert(String camelCase, List<Integer> camelCaseIndexes) {
        List<String> results = new ArrayList<>();
        int startIndex = 0;
        for (Integer index : camelCaseIndexes) {
            String part = camelCase.substring(startIndex, index);
            results.add(part.toLowerCase());
            startIndex = index;
        }

        results.add(camelCase.substring(startIndex).toLowerCase());
        return results;
    }

}
