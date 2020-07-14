package core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.AntPathMatcher;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathMatcherUtil {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public static boolean matches(String pattern, String path) {
        return PATH_MATCHER.match(pattern, path);
    }

}
