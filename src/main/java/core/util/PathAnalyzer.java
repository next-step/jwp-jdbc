package core.util;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathAnalyzer {
    private static final PathAnalyzer pathAnalyzer = new PathAnalyzer();
    private final PathPatternParser pathPatternParser = new PathPatternParser();

    private PathAnalyzer() {

    }

    public static PathAnalyzer getInstance() {
        return pathAnalyzer;
    }

    public boolean isTargetPath(String targetPath, String requestedUri) {
        PathPattern pathPattern = pathPatternParser.parse(targetPath);
        PathContainer pathContainer = PathContainer.parsePath(requestedUri);

        return pathPattern.matches(pathContainer);
    }

}
