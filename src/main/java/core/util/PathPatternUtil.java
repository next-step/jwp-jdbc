package core.util;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern.PathMatchInfo;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Map;

import static java.util.Collections.emptyMap;

public class PathPatternUtil {

    private static final PathPatternParser parser = new PathPatternParser();

    public static String getUriValue(String pattern, String path, String key) {
        final Map<String, String> uriVariables = getUriVariables(pattern, path);
        return uriVariables.get(key);
    }

    public static Map<String, String> getUriVariables(String pattern, String path) {
        PathMatchInfo pathMatchInfo = parser.parse(pattern).matchAndExtract(PathContainer.parsePath(path));

        if (pathMatchInfo == null) {
            return emptyMap();
        }

        return pathMatchInfo.getUriVariables();
    }

    public static boolean isUrlMatch(String pattern, String path) {
        return parser.parse(pattern).matches(PathContainer.parsePath(path));
    }


}
