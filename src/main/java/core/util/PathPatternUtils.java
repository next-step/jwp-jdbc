package core.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Map;

@UtilityClass
public class PathPatternUtils {
    public boolean matchPathVariables(String mappingUri, String requestUri) {
        PathPattern pp = parse(mappingUri);
        return pp.matches(toPathContainer(requestUri));
    }

    public Map<String, String> getUriVariables(String path, String requestUri) {
        return parse(path).matchAndExtract(toPathContainer(requestUri))
                .getUriVariables();
    }

    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    private PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
