package core.mvc.viewresolver;

public class ViewResolverRegistryFactory {

    public static ViewResolverRegistry create() {
        return new ViewResolverRegistry(
                new ViewNameViewResolver(),
                new JspViewResolver(),
                new HandlebarsViewResolver()
        );
    }

}
