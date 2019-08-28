package core.mvc.view;

import core.utils.FileUtils;

public class JspViewResolver implements ViewResolver {
    private final String WEB_ROOT_DIR = "webapp";
    private final String DEFAULT_PREFIX = "/";
    private final String DEFAULT_SUFFIX = ".jsp";
    private String prefix;
    private String suffix;

    public JspViewResolver() {
        this.prefix = DEFAULT_PREFIX;
        this.suffix = DEFAULT_SUFFIX;
    }

    public JspViewResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public boolean supports(String viewName) {
        String fullPath = String.format("%s%s%s%s", WEB_ROOT_DIR, this.prefix, viewName, this.suffix);
        return FileUtils.fileExists(fullPath);
    }

    @Override
    public View resolve(String viewName) {
        String fullPath = String.format("%s%s%s", this.prefix, viewName, this.suffix);
        return new JspView(fullPath);
    }
}
