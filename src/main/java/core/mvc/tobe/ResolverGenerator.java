package core.mvc.tobe;

import core.mvc.resolver.*;

import java.util.ArrayList;
import java.util.List;

public class ResolverGenerator {

    private static final List<HandlerMethodArgumentResolver> resolvers;
    static {
        resolvers = new ArrayList<>();
        resolvers.add(new ServletRequestArgumentResolver());
        resolvers.add(new ServletResponseArgumentResolver());
        resolvers.add(new RequestBodyArgumentResolver());
        resolvers.add(new JavaDataTypeArgumentResolver());
    }

    public static List<HandlerMethodArgumentResolver> getResolvers() {
        return resolvers;
    }
}
