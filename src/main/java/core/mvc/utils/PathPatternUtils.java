package core.mvc.utils;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Collections;
import java.util.Map;

public class PathPatternUtils {
    private static final PathPatternParser parser;

    private PathPatternUtils() {
    }

    static {
        parser = new PathPatternParser();
        parser.setMatchOptionalTrailingSeparator(true);
    }

    private static PathPattern parse(String url) {
        return parser.parse(url);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }

    public static boolean isMatch(String path, String targetUrl) {
        PathPattern pathPattern = parse(path);
        return pathPattern.matches(toPathContainer(targetUrl));
    }

    public static Map<String, String> extractUriVariables(String path, String targetUrl) {
        PathPattern.PathMatchInfo pathMatchInfo = parse(path).matchAndExtract(toPathContainer(targetUrl));

        if (pathMatchInfo == null) {
            return Collections.emptyMap();
        }

        return pathMatchInfo.getUriVariables();
    }
}
