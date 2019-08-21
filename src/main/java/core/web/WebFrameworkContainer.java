package core.web;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@HandlesTypes(ApplicationInitializer.class)
public class WebFrameworkContainer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> applicationInitializerClasses, ServletContext ctx) throws ServletException {
        List<ApplicationInitializer> initializers = new LinkedList<>();

        if (applicationInitializerClasses != null) {
            for (Class<?> waiClass : applicationInitializerClasses) {
                try {
                    initializers.add((ApplicationInitializer) waiClass.newInstance());
                } catch (Throwable ex) {
                    throw new ServletException("Failed to instantiate ApplicationInitializerClasses class", ex);
                }
            }
        }

        if (initializers.isEmpty()) {
            ctx.log("No ApplicationInitializerClasses types detected on classpath");
            return;
        }

        for (ApplicationInitializer initializer : initializers) {
            initializer.onStartup(ctx);
        }
    }
}
