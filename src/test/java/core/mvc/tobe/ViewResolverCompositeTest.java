package core.mvc.tobe;

import core.mvc.JspViewResolver;
import core.mvc.RedirectViewResolver;
import core.mvc.ViewResolverComposite;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class ViewResolverCompositeTest {

    private ViewResolverComposite viewResolverComposite;

    @BeforeEach
    public void initViewResolverComposite() {

        RedirectViewResolver redirectViewResolver = new RedirectViewResolver();
        JspViewResolver jspViewResolver = new JspViewResolver();

        viewResolverComposite = new ViewResolverComposite(new LinkedHashSet<>(Arrays.asList(
                redirectViewResolver, jspViewResolver
        )));
    }
}
