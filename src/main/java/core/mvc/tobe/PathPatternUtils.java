package core.mvc.tobe;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathPatternUtils {

    public static PathPattern parse(String path) {
        PathPatternParser pathPatternParser = new PathPatternParser();
        pathPatternParser.setMatchOptionalTrailingSeparator(true);
        return pathPatternParser.parse(path);
    }

    public static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }

    public static Object toPrimitive(Class targetClass, String value) {
        if (Integer.class == targetClass || int.class == targetClass) return Math.toIntExact(Long.parseLong(value));
        if (Long.class == targetClass || long.class == targetClass) return Long.parseLong(value);
        if (Boolean.class == targetClass || boolean.class == targetClass) return Boolean.parseBoolean(value);
        if (Float.class == targetClass || float.class == targetClass) return Float.parseFloat(value);
        if (Double.class == targetClass || double.class == targetClass) return Double.parseDouble(value);

        return value;
    }
}
