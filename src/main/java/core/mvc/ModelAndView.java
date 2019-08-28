package core.mvc;

import core.mvc.viewresolver.ViewResolverRegistry;
import core.mvc.viewresolver.ViewResolverRegistryFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private static final ViewResolverRegistry VIEW_RESOLVER_REGISTRY = ViewResolverRegistryFactory.create();

    private View view;
    private Map<String, Object> model = new HashMap<String, Object>();

    public ModelAndView() {
        this.view = VIEW_RESOLVER_REGISTRY.resolveView(null);
    }

    public ModelAndView(String viewName) {
        this.view = VIEW_RESOLVER_REGISTRY.resolveView(viewName);
    }

    public ModelAndView(View view) {
        this.view = view;
    }

    public ModelAndView addObject(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public Object getObject(String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    public View getView() {
        return view;
    }
}
