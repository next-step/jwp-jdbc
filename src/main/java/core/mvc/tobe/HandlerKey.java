package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.springframework.web.util.pattern.PathPattern;

public class HandlerKey {
    private String path;
    private RequestMethod requestMethod;
    private PathPattern pathPattern;

    public HandlerKey(String path, RequestMethod requestMethod) {
        this.path = path;
        this.requestMethod = requestMethod;
        this.pathPattern = PathPatternUtils.parse(path);
    }

    public boolean matches(String path, RequestMethod requestMethod) {
        return this.requestMethod == requestMethod && pathPattern.matches(PathPatternUtils.toPathContainer(path));
    }

    @Override
    public String toString() {
        return "HandlerKey [path=" + path + ", requestMethod=" + requestMethod + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((requestMethod == null) ? 0 : requestMethod.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HandlerKey other = (HandlerKey) obj;
        if (requestMethod != other.requestMethod)
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }
}
