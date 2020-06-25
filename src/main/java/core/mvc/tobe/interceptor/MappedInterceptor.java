package core.mvc.tobe.interceptor;

import core.util.PathPatternUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MappedInterceptor implements HandlerInterceptor {
    protected List<String> matchingUris = new ArrayList<>();

    public MappedInterceptor(String... matchingUris) {
        if (ArrayUtils.isNotEmpty(matchingUris)) {
            this.matchingUris.addAll(Arrays.asList(matchingUris));
        }
    }

    @Override
    public boolean matches(String requestUri) {
        return matchingUris.stream().anyMatch(uri -> PathPatternUtil.isUrlMatch(uri, requestUri));
    }
}
